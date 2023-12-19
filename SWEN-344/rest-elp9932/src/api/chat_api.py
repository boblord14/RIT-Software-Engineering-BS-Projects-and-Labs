from flask_restful import Resource
from flask_restful import request
from flask_restful import reqparse
import datetime
from db import chat

class HelloWorld(Resource):
    def get(self):
        return ("yeah this works")
    
class communities(Resource):
    def get(self):
        rawData = chat.getCommunityData()
        cleanedData = {}
        for channelData in rawData:
            if(cleanedData.get(channelData[1], None) == None):
                cleanedData[channelData[1]] = [channelData[0]]
            else:
                cleanedData[channelData[1]] = cleanedData.get(channelData[1]) + [channelData[0]]
        return cleanedData
    
class searchMsg(Resource):
    def get(self):
        msgToSend=request.args.get('msg')
        sizeToSend=request.args.get('size')
        dateStartToSend=request.args.get('start')
        dateEndToSend=request.args.get('end')

        rawData = chat.msgContentAndUserIDSearch(msgToSend, sizeToSend, dateStartToSend, dateEndToSend)
        
        cleanedData = []
        for messageData in rawData:
            if(messageData[3]==None): #split on variants for dms and channel messages
                cleanedData.append(dict(Sender=messageData[0], Recipient=messageData[1], Message=messageData[4], Timestamp=messageData[5].strftime("%m/%d/%Y")))
            else:
                cleanedData.append(dict(Sender=messageData[0], Channel=messageData[2], Community=messageData[3], Message=messageData[4], Timestamp=messageData[5].strftime("%m/%d/%Y")))
        return cleanedData
    
class getChannelMessages(Resource):
    def get(self):
        channelID=request.args.get('id')
        rawData = chat.getMessagesInChannelID(channelID)
        cleanedData = []
        for messageData in rawData:
            cleanedData.append(dict(Sender=messageData[0], Channel=messageData[1], Community=messageData[2], Message=messageData[3], Timestamp=messageData[4].strftime("%m/%d/%Y")))
        return cleanedData

class user(Resource):
    def get(self):
        rawData = chat.getAllUsers()
        cleanedData = []
        for userData in rawData:
            if(userData[3] != None):
                cleanedData.append(dict(userID=userData[0], userName=userData[1], phoneNum=userData[2], usernameLastChangeData=userData[3].strftime("%m/%d/%Y")))
            else:
                cleanedData.append(dict(userID=userData[0], userName=userData[1], phoneNum=userData[2], usernameLastChangeData=userData[3]))
        return cleanedData
    
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('username', type=str)
        parser.add_argument('phoneNumber', type=str)
        parser.add_argument('password', type=str)
        args = parser.parse_args()
        if(chat.createUser(args['username'], args['phoneNumber'], args['password']) == -1): return "Invalid username, user already exists", 400
        else: return "user successfully created", 200
    
    def put(self):
        parser = reqparse.RequestParser()
        parser.add_argument('newUsername', type=str)
        parser.add_argument('newPhoneNum', type=str)
        parser.add_argument('newPassword', type=str)
        args = parser.parse_args()

        key = request.headers.get('session')
        userID = chat.validateOperation(key)
        if (userID == -1): return '', 401

        if(args['newPhoneNum'] != None): 
            chat.updatePhoneNum(chat.getUsernameFromID(userID), args['newPhoneNum'])
        
        if(args['newPassword'] != None): 
            chat.updatePassword(chat.getUsernameFromID(userID), args['newPassword'])

        if(args['newUsername'] != None):
            if(chat.updateUsername(chat.getUsernameFromID(userID), args['newUsername'], datetime.date.today()) == -1):
                return "Error: invalid new username or 1 change per 6 months exceeded", 400
        return "User updated successfully", 200
    
    def delete(self):
        key = request.headers.get('session')
        userID = chat.validateOperation(key)
        if (userID == -1): return '', 401

        chat.deleteUser(chat.getUsernameFromID(userID))
        return "user successfully deleted"
    
class dms(Resource):
    def get(self):
        otherUser=request.args.get('otherUser')
        numMsgs=request.args.get('num')

        key = request.headers.get('session')
        userID = chat.validateOperation(key)
        if (userID == -1): return '', 401

        dmList = chat.readDMConversation(otherUser, chat.getUsernameFromID(userID))
        if(numMsgs != None): del dmList[:(-1*int(numMsgs))] #trim list down to num wanted

        cleanedData = [] #clean up for output
        for messageData in dmList:
            cleanedData.append(dict(Sender=messageData[0], Recipient=messageData[1], Message=messageData[4], Timestamp=messageData[5].strftime("%m/%d/%Y")))
        return cleanedData
    
    def post(self):
        otherUser=request.args.get('otherUser')

        parser = reqparse.RequestParser()
        parser.add_argument('msg', type=str)
        args = parser.parse_args()

        key = request.headers.get('session')
        userID = chat.validateOperation(key)
        if (userID == -1): return '', 401

        chat.sendDM(chat.getUsernameFromID(userID), otherUser, args['msg'], None)
        return f"DM sent successfully", 200
    
class login(Resource):
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('username', type=str)
        parser.add_argument('password', type=str)
        args = parser.parse_args()
        key = chat.login(args['username'], args['password'])
        if(key==-1): return "Invalid username or password", 400
        else: return key, 200
    
    def delete(self):
        key = request.headers.get('session')
        userID = chat.validateOperation(key)
        if (userID == -1): return 'Invalid session, please log in again and get a valid session key', 401

        chat.logout(userID)
        return "successful logout. must log in again to resume operations", 200
