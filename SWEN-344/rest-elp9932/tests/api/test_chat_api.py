import unittest
import json
import datetime
from tests.test_utils import *


class TestChatApi(unittest.TestCase):

    def setUp(self):  
        """Initialize/fill DB using API call"""
        post_rest_call(self, 'http://localhost:5000/manage/init')
        post_rest_call(self, 'http://localhost:5000/manage/load')

    def test_list_all_users(self):
        """Test the api call to list all the users in the db"""
        expected = 9
        actual = get_rest_call(self, 'http://127.0.0.1:5000/user')
        self.assertEqual(expected, len(actual), "The API should return a total of 9 users")

    def test_list_all_communities_channels(self):
        """Test the api call to list all channels and their communities"""
        actual = get_rest_call(self, 'http://127.0.0.1:5000/communities')
        self.assertEqual(2, len(actual), "The API should return a total of 2 channels")
        self.assertEqual(2, len(list(actual.values())[0]), "There should be 2 channels in Arrakis")
        self.assertEqual(2, len(list(actual.values())[1]), "There should be 2 channels in Comedy")
        
    def test_list_all_msgs_channel(self):
        """Test the api call to list all messages in a specific channel"""
        actual = get_rest_call(self, 'http://127.0.0.1:5000/channel?id=4')
        self.assertEqual(2, len(actual), "There should be 2 messages in the channel with ID 4(#Dialogs channel in Comedy)")
        actual = get_rest_call(self, 'http://127.0.0.1:5000/channel?id=8')
        self.assertEqual([], actual, "There should be no messages in an invalid channel ID")

    def test_search_message_api(self):
        """Test the api call to the search message"""
        actual = get_rest_call(self, 'http://127.0.0.1:5000/searchMsg?msg=reply')
        self.assertEqual(3, len(actual), "There should be 3 messages found with \"reply\" in them")
        actual = get_rest_call(self, 'http://127.0.0.1:5000/searchMsg?msg=test&start=2-2-1995&end=2-3-1995')
        self.assertEqual(2, len(actual), "There should be 2 messages found with \"test\" in them between dates 2-2-1995 and 2-3-1995(inclusive)")

    def  test_user_add(self):
        """Test the api call to add a new user"""

        data = dict(username="Ethan", phoneNumber="123-456-7890", password="testpass")
        jdata = json.dumps(data)
        hdr = {'content-type': 'application/json'}

        actual = post_rest_call(self, 'http://127.0.0.1:5000/user', jdata, hdr, 200)
        self.assertEqual("user successfully created", actual, "User did't actually get created, error thrown")

        actual = get_rest_call(self, 'http://127.0.0.1:5000/user')
        data = dict(userID=10, userName='Ethan', phoneNum='123-456-7890', usernameLastChangeData=None)
        self.assertEqual(data, actual[9], "The user did not get put in the db properly")
        #i know im not checking the sha512 hashing but that should be tested by nature in the later tests. 

    def test_invalid_user_add(self):
        """Test adding an invalid user and getting an error msg/400 code as a response"""
        data = dict(username="Moe", phoneNumber="402-593-1923", password="pass3")
        jdata = json.dumps(data)
        hdr = {'content-type': 'application/json'}

        actual = post_rest_call(self, 'http://127.0.0.1:5000/user', jdata, hdr, 400)
        self.assertEqual("Invalid username, user already exists", actual, "User somehow was added despite their username already being in the database")
    
    def test_login_and_hashing(self):
        """Test the api call to log in and get the session key, as well as the result of an invalid login"""
        data = dict(username="Moe", password="pass3")
        jdata = json.dumps(data)
        hdr = {'content-type': 'application/json'}

        actual = post_rest_call(self, 'http://127.0.0.1:5000/auth', jdata, hdr, 200)
        self.assertNotEqual(-1, actual, "A valid session key was NOT returned") #cant exactly compare it for a valid session key due to the randomization, but i can test for the lack thereof.  

        data = dict(username="Moe", password="badpassword")
        jdata = json.dumps(data)
        hdr = {'content-type': 'application/json'}

        actual = post_rest_call(self, 'http://127.0.0.1:5000/auth', jdata, hdr, 400)
        self.assertEqual("Invalid username or password", actual, "Invalid login not registering as \'invalid\'")

    def test_edit_user(self):
        """Tests the api call that editing username/phone num/password works"""
        key = post_rest_call(self, 'http://127.0.0.1:5000/auth', json.dumps(dict(username="Moe", password="pass3")), {'content-type': 'application/json'}) #one line logon
        hdr = {'content-type': 'application/json', 'session': key}
        jdata = json.dumps(dict(newUsername="FakeMoe", newPassword="Fakepass3", newPhoneNum="157-742-2515"))
        put_rest_call(self, 'http://127.0.0.1:5000/user', jdata, hdr, 200)

        actual = get_rest_call(self, 'http://127.0.0.1:5000/user') #check that its valid
        dateShift = datetime.date.today() + datetime.timedelta(days=183)
        data = dict(userID=3, userName='FakeMoe', phoneNum='157-742-2515', usernameLastChangeData=dateShift.strftime("%m/%d/%Y"))
        self.assertEqual(data, actual[8], "The user did not get updated in the db properly")

        key2 = post_rest_call(self, 'http://127.0.0.1:5000/auth', json.dumps(dict(username="FakeMoe", password="Fakepass3")), {'content-type': 'application/json'}) #still need to check some password things
        self.assertNotEqual(-1, key2, "New login info not returning a valid session key")
        self.assertNotEqual(key, key2, "Original session key should NOT equal the new session key with the new login")

        put_rest_call(self, 'http://127.0.0.1:5000/user', jdata, hdr, 401) #testing a non existent user. this uses the old session key. Session keys are tied to the users themselves and as a result this serves like trying to edit an invalid user, and as such doesnt work.

        jdata = json.dumps(dict(newUsername="Larry", newPassword="Fakepass3", newPhoneNum="157-742-2515")) #one more test, making sure you cant change your username to someone else's username
        hdr = {'content-type': 'application/json', 'session': key2}
        put_rest_call(self, 'http://127.0.0.1:5000/user', jdata, hdr, 400)

    def test_remove_user(self):
        """Tests the api call for deleting users, and making sure you can't delete any other existing/non existing users other than yourself"""
        key = post_rest_call(self, 'http://127.0.0.1:5000/auth', json.dumps(dict(username="Moe", password="pass3")), {'content-type': 'application/json'}) #one line logon
        hdr = {'content-type': 'application/json', 'session': key}
        delete_rest_call(self, 'http://127.0.0.1:5000/user', hdr, 200)

        actual = get_rest_call(self, 'http://127.0.0.1:5000/user') #check that its valid
        self.assertEqual(8, len(actual), "The user did not get deleted from the db")

        delete_rest_call(self, 'http://127.0.0.1:5000/user', hdr, 401) #doing it again, making sure that trying to delete a non existant user actually does nothing. 
        self.assertEqual(8, len(actual), "Somehow extra stuff got deleted from the db") #also since the session key is now invalid(deleted from db), this serves as a test for trying to delete another user as well, which fails due to invalid session key

    def test_get_dms(self):
        """Tests the api call for getting dms from another user, with and without quantities"""
        key = post_rest_call(self, 'http://127.0.0.1:5000/auth', json.dumps(dict(username="Moe", password="pass3")), {'content-type': 'application/json'}) #one line logon
        hdr = {'content-type': 'application/json', 'session': key}
        actual = get_rest_call(self, 'http://127.0.0.1:5000/dms?otherUser=Paul', {}, hdr) #should get all dms between the two
        self.assertEqual(4, len(actual), "There should be a total of 4 dms between paul and moe")

        actual = get_rest_call(self, 'http://127.0.0.1:5000/dms?otherUser=Paul&num=2', {}, hdr) #just get 2 this time
        self.assertEqual(2, len(actual), "There should be 2 of the 3 total dms between paul and moe")        

    def test_send_dms(self):
        """Tests the api call for sending a dm to a user"""
        key = post_rest_call(self, 'http://127.0.0.1:5000/auth', json.dumps(dict(username="Moe", password="pass3")), {'content-type': 'application/json'}) #one line logon
        hdr = {'content-type': 'application/json', 'session': key}
        jdata = json.dumps(dict(msg="heres a REST message"))
        post_rest_call(self, 'http://127.0.0.1:5000/dms?otherUser=Paul', jdata, hdr) #queue up a dm to send to paul

        actual = get_rest_call(self, 'http://127.0.0.1:5000/dms?otherUser=Paul', {}, hdr) #should get all dms between the two
        self.assertEqual(5, len(actual), "There should be a total of 5 dms between paul and moe")
        self.assertEqual("heres a REST message", actual[4]['Message'], "Latest message should read \"heres a REST message\"")

    def test_logout(self):
        """Tests the logout capabilities"""
        delete_rest_call(self, 'http://127.0.0.1:5000/auth', {'content-type': 'application/json', 'session': 'fakekeylmao'}, 401) #testing logout with a bad session key

        key = post_rest_call(self, 'http://127.0.0.1:5000/auth', json.dumps(dict(username="Moe", password="pass3")), {'content-type': 'application/json'}) #valid login
        delete_rest_call(self, 'http://127.0.0.1:5000/auth', {'content-type': 'application/json', 'session': key}, 200) #valid logout

        delete_rest_call(self, 'http://127.0.0.1:5000/auth', {'content-type': 'application/json', 'session': key}, 401) #running it again should spit 401 again as its invalid now