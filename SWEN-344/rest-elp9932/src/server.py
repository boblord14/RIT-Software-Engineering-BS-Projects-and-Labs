from flask import Flask
from flask_restful import Resource, Api
from api.chat_api import *
from api.management import *

app = Flask(__name__)
api = Api(app)

api.add_resource(Init, '/manage/init') #Management API for initializing the DB

api.add_resource(Load, '/manage/load') #Management API for  loading the test data into the DB

api.add_resource(Version, '/manage/version') #Management API for checking DB version

api.add_resource(communities, '/communities')  #community data from db

api.add_resource(searchMsg, '/searchMsg') #search for specific message content

api.add_resource(getChannelMessages, '/channel') #get messages in a given channel

api.add_resource(HelloWorld, '/') #dummy generic test

api.add_resource(user, '/user') #crud operations for user, merged with the getusers from rest1

api.add_resource(dms, '/dms') #send and get dms

api.add_resource(login, '/auth') #log in and out as user



if __name__ == '__main__':
    rebuildTables()
    buildFullTestData()
    app.run(debug=True)