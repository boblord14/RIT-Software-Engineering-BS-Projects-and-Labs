from flask_restful import Resource, request, reqparse
from db.swen344_db_utils import *

class Inventory(Resource):
#get handles a variety of parameters.  Parameters are sent as query strings
#There can be zero or one (single) argument
#If there is an argument, it is used to filter the query
#e.g. ?type={somevalue}, where {somevalue} ... without the {} ... is the filter value applied to 'type'
    def get(self):
        print('get')
        model=request.args.get('model')
        type=request.args.get('type')
        return exec_get_all("""SELECT MANUFACTURER.NAME, INVENTORY.MODEL, EV_TYPE.TYPE, INVENTORY.DESCRIPTION, INVENTORY.QUANTITY
                                FROM ((INVENTORY INNER JOIN MANUFACTURER ON INVENTORY.MANF_ID = MANUFACTURER.ID)
                                INNER JOIN EV_TYPE ON INVENTORY.TYPE_ID = EV_TYPE.ID)
                                WHERE ((%s IS NULL OR INVENTORY.MODEL = %s) AND (%s IS NULL OR(to_tsvector(EV_TYPE.TYPE) @@ to_tsquery(%s)))) ORDER BY EV_TYPE.TYPE ASC""",
                                (model, model, type, type))

    
#post handles new additions to the data.  All data is sent in the body of the HTTP command
#The client sends the required parameters as strings in the json body.  If any foreign keys are needed
#the server (this code) handles getting those keys
    def post(self):
        print("post")
        parser = reqparse.RequestParser()
        parser.add_argument('Manufacturer', type=str)
        parser.add_argument('Type', type=str)
        parser.add_argument('Model', type=str)
        parser.add_argument('Description', type=str)
        parser.add_argument('Quantity', type=int)
        args = parser.parse_args()
        exec_commit("""INSERT INTO INVENTORY ( MANF_ID, MODEL, TYPE_ID, DESCRIPTION, QUANTITY) VALUES 
                    ((SELECT ID FROM MANUFACTURER WHERE NAME = %s), %s, (SELECT ID FROM EV_TYPE WHERE TYPE = %s), %s, %s)""",
                    (args['Manufacturer'], args['Model'], args['Type'], args['Description'], args['Quantity']))

#put handles updates to the data.  All data is sent in the body of the HTTP command
#The client sends the required parameters as strings.
#The server code (here) is responsible for getting the corresponding IDs (as needed) to 
#perform the command
    def put(self):
        print("put")
        parser = reqparse.RequestParser()
        parser.add_argument('Model', type=str)
        parser.add_argument('Quantity', type=int)
        args = parser.parse_args()
        exec_commit("""UPDATE INVENTORY
         SET QUANTITY = %s
         WHERE MODEL = %s""",(args['Quantity'], args['Model']))


#Used to completely remove a model from the inventory
    def delete(self):
        print("delete")
        parser = reqparse.RequestParser()
        parser.add_argument('name', type=str)
        args = parser.parse_args()
        exec_commit("DELETE FROM INVENTORY WHERE MODEL = %s", (args['name'],))
        #apparently i dont need this but i was bored so why not
