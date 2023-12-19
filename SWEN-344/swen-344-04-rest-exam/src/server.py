from flask import Flask
from flask_restful import Resource, Api
from api.manufacturer import *
from api.inventory import *
from db.init_db import insert_test_data

app = Flask(__name__)
api = Api(app)

api.add_resource(Manufacturer, '/manufacturer')
api.add_resource(ManufacturerId,'/manufacturer/<int:id>')
api.add_resource(ManufacturerName,'/manufacturer/name/<string:name>')
api.add_resource(Inventory, '/inventory')

if __name__ == '__main__':
    insert_test_data()
    app.run(debug=True)
