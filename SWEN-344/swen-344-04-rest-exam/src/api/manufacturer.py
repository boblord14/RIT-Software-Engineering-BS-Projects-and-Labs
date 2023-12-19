from flask_restful import Resource
from db.swen344_db_utils import exec_get_all

class Manufacturer(Resource):
    def get(self):
        return exec_get_all('SELECT * FROM MANUFACTURER')
    
class ManufacturerId(Resource):
    def get(self, id):
        return exec_get_all("SELECT * FROM MANUFACTURER WHERE ID=%s", str(id))

class ManufacturerName(Resource):
    def get(self, name):
        result = exec_get_all("SELECT * FROM MANUFACTURER WHERE name=%s", (name,))
        return result
