from flask_restful import Resource, reqparse, request  #NOTE: Import from flask_restful, not python

from db.swen344_db_utils import *

from db.chat import rebuildTables
from db.chat import buildFullTestData

class Init(Resource):
    def post(self):
        rebuildTables()

class Version(Resource):
    def get(self):
        return (exec_get_one('SELECT VERSION()'))
    
class Load(Resource):
    def post(self):
        buildFullTestData()