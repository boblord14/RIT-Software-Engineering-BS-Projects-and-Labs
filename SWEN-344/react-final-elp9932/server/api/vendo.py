from flask_restful import Resource

from flask_restful import request
from flask_restful import reqparse
import json
from .swen_344_db_utils import *

class Vendo(Resource):
    def get(self):
        result = exec_get_all("""SELECT item.name, item.price::float8::numeric::money, vending_machine.row_number, vending_machine.position_number, buttons.button_label, vending_machine.quantity 
                              FROM ((vending_machine INNER JOIN item ON vending_machine.item_id = item.id) 
                              INNER JOIN buttons ON vending_machine.button_id = buttons.id)
                              ORDER BY buttons.id""")
        return result
    
    def put(self):
        parser = reqparse.RequestParser()
        parser.add_argument('buttonPress', type=str)
        parser.add_argument('updateMachine', type=dict)
        args = parser.parse_args()
        if(args['buttonPress'] != None):
            exec_commit("""UPDATE vending_machine 
                        SET quantity=(vending_machine.quantity-1)
                        FROM buttons
                        WHERE buttons.id = vending_machine.button_id
                        AND buttons.button_label=%s
                        AND vending_machine.quantity>=1""", (args['buttonPress']))
        if(args['updateMachine'] != None):
            data = args['updateMachine']
            lastID = exec_get_one("""SELECT MAX(id) FROM item""")
            exec_commit("""UPDATE vending_machine 
                        SET quantity=%s, item_id=%s
                        FROM buttons
                        WHERE buttons.id = vending_machine.button_id
                        AND buttons.button_label=%s""", (data['quantity'], lastID[0], data['button']))
        return

    def post(self):   
        parser = reqparse.RequestParser()
        parser.add_argument('newProduct', type=dict)
        args = parser.parse_args()
        newProduct = args['newProduct']
        exec_commit("""INSERT INTO item VALUES (DEFAULT, %s, %s)""", (newProduct['name'], newProduct['price']))
        return


