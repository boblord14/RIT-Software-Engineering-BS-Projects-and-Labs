from flask_restful import Resource
from flask_restful import request
from flask_restful import reqparse
import json
from .swen_344_db_utils import *
import api.club_db as club

class ClubApi(Resource):
    def get(self):
        city=request.args.get('city')
        result = club.getClubs(city)
        return result
    
    def delete(self):
        parser = reqparse.RequestParser()
        parser.add_argument('id', type=int)
        args = parser.parse_args()
        club.deleteClub(args['id'])
    
    def put(self):
        parser = reqparse.RequestParser()
        parser.add_argument('club', type=dict)
        args = parser.parse_args()
        club.updateClub(args['club'])

    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('club', type=dict)
        args = parser.parse_args()
        club.createClub(args['club'])
