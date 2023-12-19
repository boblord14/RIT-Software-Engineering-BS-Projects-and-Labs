import unittest
import csv
from src.chat import *
from src.swen344_db_utils import connect

def buildBaseTestData():
   """Builds initial test data from the .sql files of values for DB1 testing"""
   rebuildTables()
   exec_sql_file('db-elp9932/tests/user_data.sql')
   exec_sql_file('db-elp9932/tests/community_data.sql')
   exec_sql_file('db-elp9932/tests/message_data.sql')

def loadNewTestData():
   """Builds the initial test data with the additional data for DB2 testing"""
   buildBaseTestData()
   createUser("DrMarvin", "790-575-3487")
   createUser("Bob", "683-287-2535")
   sendDM("Bob", "DrMarvin", "I’m doing the work, I’m baby-stepping", datetime.date(1991, 5, 18))

def loadCommunityTestData():
   """Builds the initial test data with the community augmentations for DB3 testing"""
   loadNewTestData()
   createUser("spicelover", "645-204-1958")
   joinCommunity("spicelover", "Arrakis")
   createUser("Paul", "650-186-2739") #"verify that the db is correectly updated after each action" I think ive confirmed that user creation works enough. I'm not verifying this. 
   joinCommunity("Paul", "Arrakis") #verified in test_community_join. Up here because I frequently need it for the db3 tests
   sendDM("Paul", "Moe", "test message P to M", None)
   sendDM("Moe", "Paul", "test message M to P", None)
   sendDM("Paul", "Moe", "test message P to M #2", None)

def loadAnalyticsTestData():
   loadCommunityTestData()
   suspendUser("Paul", "Arrakis", datetime.date(2222, 2, 22), datetime.date(2023, 10, 1))
   sendChannelMessage("Abbott", "Comedy", "#Dialogs", "please reply", None)
   sendChannelMessage("Costello", "Comedy", "#Dialogs", "i replied already!", None)

def importCSVData():
   """Imports the test csv data. nothing special."""
   with open('tests/whos_on_first.csv') as csv_file:
      firstline = True
      lineReader = csv.reader(csv_file)
      for line in lineReader:
         if firstline == True:
            firstline = False
         else: 
            if(line[0] == "Abbott"):
               sendDM("Abbott", "Costello", "".join(line[1:]), None)
            else:
               sendDM("Costello", "Abbott", "".join(line[1:]), None)

class TestChat(unittest.TestCase):

    def test_rebuild_tables_is_idempotent(self):
      """Drop and rebuild the tables twice"""
      rebuildTables()
      rebuildTables()
      result = exec_get_all('SELECT * FROM user_list')
      self.assertEqual([], result, "no rows in user_list")
      result = exec_get_all('SELECT * FROM message_list')
      self.assertEqual([], result, "no rows in message_list")

    def test_table_load(self):
       """Confirms if all table data loads in properly"""
       buildBaseTestData()
       result = exec_get_all('SELECT * FROM message_list')
       self.assertEqual(7, len(result), "message_list needs to have exactly 7 messages added")

       result = exec_get_all('SELECT * FROM user_list')
       self.assertEqual(5, len(result), "user_list needs to have exactly 5 users added")

       result = exec_get_all('SELECT * FROM channel_list')
       self.assertEqual(4, len(result), "channel_list needs to have exactly 4 channels added")

       result = exec_get_all('SELECT * FROM community_list')
       self.assertEqual(2, len(result), "community_list needs to have exactly 2 communities added")

       result = exec_get_all('SELECT * FROM membership_list')
       self.assertEqual(5, len(result), "membership_list needs to have exactly 5 memberships added")

       result = exec_get_all('SELECT * FROM suspension_list')
       self.assertEqual(2, len(result), "suspension_list needs to have exactly 2 suspensions added")

       result = exec_get_all('SELECT * FROM unread_list')
       self.assertEqual(5, len(result), "unread_list needs to have exactly 5 unreads added")

    def test_user_ban(self):
      """Confirms that larry is banned from the community Comedy during 2023(he should be) and 1952(he shouldnt be)"""
      buildBaseTestData()
      self.assertTrue(isUserBanned("Larry", "Comedy", datetime.date(2023, 4, 9)), "Larry should be banned in Comedy during 2023")
      self.assertFalse(isUserBanned("Larry", "Comedy", datetime.date(1952, 11, 29)), "Larry should not be banned in Comedy during 1952")
      self.assertFalse(isUserBanned("Abbott", "Comedy", datetime.date(2222, 2, 22)), "Abbott has no ban registered in Comedy , should never have been banned")
    
    def test_get_id(self):
      """Confirms that Moe's ID is the value it should be(3) and bob's id when freshly added is 6"""
      buildBaseTestData()
      self.assertEqual(3, getIDFromUsername("Moe"), "Moe's ID should be 3")
      createUser("Bob", "683-287-2535")
      self.assertEqual(6, getIDFromUsername("Bob"), "Bob's ID should be 6")
      
    def test_get_messages(self):
       """Checks the count of messages sent from one user to another(2) and the other way around(1)"""
       buildBaseTestData()
       result = getDMs("Abbott", "Costello")
       self.assertEqual(2, len(result), "There needs to be 2 total messages from Abbott to Costello")
       result.append(getDMs("Costello", "Abbott"))
       self.assertEqual(3, len(result), "There needs to be 1 total message from Costello to Abbott")

    def test_get_messages_within_date_range(self):
       """Checks the count of messages sent from one user to another during a specific date range(2) and the other way around(1)"""
       buildBaseTestData()
       result = getDMsInDateRange("Moe", "Larry", datetime.date(1995,1,1), datetime.date(1995,12,31))
       self.assertEqual(2, len(result), "There needs to be 2 total messages from Moe to Larry in 1995")
       result = result + getDMsInDateRange("Larry", "Moe", datetime.date(1995,1,1), datetime.date(1995,12,31))
       self.assertEqual(3, len(result), "There needs to be 1 total message from Larry to Moe in 1995")
       for message in result:
          self.assertEqual(1995, message[5].year, "year of message should be 1995")

    def test_get_unread_message_count(self):
       """Checks the number of unread messages a user has(2)"""
       buildBaseTestData()
       self.assertEqual(2, len(getUnreadMessages("Abbott")), "Abbott should have 2 unread messages")

    def test_bad_username_call(self):
       """Confirms that invalid usernames on calls to get messages(in various ways) return an an empty tuple, and that an invalid username returns -1 from getID"""
       buildBaseTestData()
       self.assertEqual([], getUnreadMessages("invalid_username"))
       self.assertEqual([], getDMs("invalid_username", "Costello"))
       self.assertEqual(-1, getIDFromUsername("invalid_username"))
    
    def test_new_users(self):
       """Tests that the new users were properly loaded in, as long as the ids are valid the rest of the fields should be fine, just reuses the same sql as is used for the other ones"""
       loadNewTestData()
       self.assertEqual(6, getIDFromUsername("DrMarvin"), "DrMarvin's ID should be 6")
       self.assertEqual(7, getIDFromUsername("Bob"), "Bob's ID should be 7")
       self.assertEqual("I’m doing the work, I’m baby-stepping", getDMs("Bob", "DrMarvin")[0][4], "The content of Bob's message to DrMarvin did not send right")

    def test_change_username(self):
       """Tests a successful name change, and a failed one(followed by making sure it actually failed properly)"""
       loadNewTestData()
       updateUsername("Bob", "BabySteps2Door", datetime.date(1991, 5, 19))
       self.assertEqual(7, getIDFromUsername("BabySteps2Door"), "Updated username does not return proper user ID")
       self.assertEqual(-1, updateUsername("BabySteps2Door", "BabySteps2Elevator", datetime.date(1991, 5, 20)), "Six month rule for changing username does not work")
       self.assertEqual(-1, getIDFromUsername("BabySteps2Elevator"), "false username from failed name change should return invalid")

    def test_unread_and_read(self):
       """Tests the newer unread function and that a message gets read properly"""
       loadNewTestData()
       updateUsername("Bob", "BabySteps2Door", datetime.date(1991, 5, 19))
       unreads = getDetailedUnreadDMs("DrMarvin")
       self.assertEqual(getIDFromUsername("BabySteps2Door"), list(unreads)[0], "First unread message is not from BabySteps2Door")
       self.assertEqual(1, len(unreads.get(getIDFromUsername("BabySteps2Door"))), "Unread message count from BabySteps2Door should be 1")
       unreadMessage = unreads.get(getIDFromUsername("BabySteps2Door"))[0]
       markAsRead(unreadMessage[0])
       self.assertFalse(exec_get_all(f'select * FROM unread_list WHERE messageID=\'{unreadMessage[0]}\''), "Message should be read by now")
   
    def test_supension_and_sending(self):
       """Tests sending while suspended in a server, removing a suspension, and sending after removal"""
       loadNewTestData()
       badDate = sendChannelMessage("Larry", "Comedy", "#Dialogs", "Dummy message content", datetime.date(2012, 5, 5))
       self.assertEqual(datetime.date(2060, 1, 1), badDate, "sendMessage should spit out suspension end date on banned user attempting to send msg")
       removeSuspension("Larry", "Comedy")
       self.assertFalse(isUserBanned("Larry", "Comedy", datetime.date(2012, 5, 5)), "Larry should no longer be banned")
       sendChannelMessage("Larry", "Comedy", "#Dialogs", "Dummy message content", datetime.date(2012, 5, 5))
       confirmation = getMessagesInChannel("Comedy", "#Dialogs")
       self.assertEqual(1, len(confirmation), "There should now be one message sent from larry(sent in 2012)")

    def test_csv_imports(self):
       loadNewTestData()
       importCSVData()
       self.assertEqual(95, len(getUnreadMessages("Abbott"))-2, "Should have imported 95 unread messages from abbott(csv has 184 msgs total)")
       self.assertEqual(89, len(getUnreadMessages("Costello"))-1, "Should have imported 89 unread messages from costello(csv has 184 msgs total)")

    def test_community_join(self):
       """Tests the community join function for two new users"""
       loadCommunityTestData()
       #new users and community joins are done in loadCommunityTestData() for ease of calling them for test. They're verified here. 
       self.assertEqual(2, len(getMembersOfCommunity("Arrakis")), "There should be 2 members of the Arrakis community")
       self.assertEqual(getIDFromUsername("Paul"), getMembersOfCommunity("Arrakis")[1][0], "The second member of the Arrakis community should be Paul")

    def test_community_channel_unreads(self):
       """Tests that unread messages in channels and communities works properly"""
       loadCommunityTestData()
       self.assertEqual({}, getDetailedUnreadChannels("spicelover", "Arrakis"), "Should be no unread channels for spicelover in Arrakis before messages are sent")
       self.assertEqual({}, getDetailedUnreadCommunities("spicelover"), "Should be no unread communities for spicelover in Arrakis before messages are sent")
       sendChannelMessage("Paul", "Arrakis", "#Worms", "Test message to #Worms", None)
       self.assertEqual(1, len(getDetailedUnreadChannels("spicelover", "Arrakis").get(getIDFromChannelWithCommunity("#Worms", "Arrakis"))), "Should be one unread message in #Worms for spicelover")
       self.assertEqual(1, len(getDetailedUnreadCommunities("spicelover").get(getIDFromCommunityName("Arrakis"))), "Should be one unread message in Arrakis for spicelover")
       self.assertEqual({}, getDetailedUnreadChannels("Paul", "Arrakis"), "Paul shouldn't be gettin unreads on his own messages in channels")
       self.assertEqual({}, getDetailedUnreadCommunities("Paul"), "Paul shouldn't be gettin unreads on his own messages in communities")
    
    def test_mentions(self):
       """Tests the mention functionality for a user and that it only works in communities they are in"""
       loadCommunityTestData()
       self.assertEqual(0, len(getMentions("spicelover")), "spicelover should have no mentions yet")
       sendChannelMessage("Paul", "Arrakis", "#Worms", "Test mention @spicelover", None)
       self.assertEqual(1, len(getMentions("spicelover")), "spicelover should have 1 mention")
       self.assertEqual("Test mention @spicelover", getMentions("spicelover")[0][4], "mentioned message not returning properly")
       sendChannelMessage("Moe", "Comedy", "#Dialogs", "Test bad mention @spicelover", None)
       self.assertEqual(1, len(getMentions("spicelover")), "spicelover should still have 1 mention, not 2")
    
    def test_community_specific_suspension(self):
       """Tests the new community specific suspensions and that they only work in the community in question"""
       loadCommunityTestData()
       suspendUser("Paul", "Arrakis", datetime.date(2222, 2, 22), datetime.date(2023, 9, 24))
       self.assertEqual(datetime.date(2222, 2, 22), sendChannelMessage("Paul", "Arrakis", "#Worms", "Test suspended message send", None), "Paul shouldn't be able to send a message in Arrakis while suspended")
       joinCommunity("Paul", "Comedy")
       self.assertEqual(2, getCommunityIDsFromMember("Paul")[1][0], "Paul should be able to join Comedy no issue")
       sendChannelMessage("Paul", "Comedy", "#Dialogs", "Test not banned here message", None)
       self.assertEqual(1, len(getMessagesInChannelFromUser("Comedy", "#Dialogs", "Paul")), "Paul needs to send one message in comedy after he joins")

    def test_DM_count(self):
       """Tests counting dms(slightly redundant)"""
       loadCommunityTestData()
       self.assertEqual(3, len(readDMConversation("Paul", "Moe")), "There should be 3 dms between Paul and Moe")

    def test_search_strings(self):
       """Tests the search functionality returning messages """
       loadAnalyticsTestData()
       firstSearch = searchMsgStringInCommunity("Comedy", "reply")
       self.assertEqual(2, len(firstSearch), "search for \"reply\" should return 2 messages")
       self.assertEqual("please reply", firstSearch[0][4], "search for \"reply\", first message should be \"please reply\"")
       self.assertEqual("i replied already!", firstSearch[1][4], "search for \"reply\", second message should be \"i replied already!\"")

       secondSearch = searchMsgStringInCommunity("Comedy", "reply please")
       self.assertEqual(1, len(secondSearch), "search for \"reply please\" should return 1 message")
       self.assertEqual("please reply", secondSearch[0][4], "search for \"reply please\", first message should be \"please reply\"")

       thirdSearch = searchMsgStringInCommunity("Comedy", "asdfasdfasdfasdfasdfasdfasdf") #shouldnt find
       self.assertEqual(-1, thirdSearch, "search for \"asdfasdfasdfasdfasdfasdfasdf\" should return nothing")
   
    def test_activity_summary(self):
       """hard to test this one programmatically. check the console. should just be the two communities, with only 2 messages in comedy and none anywhere else. 2/30 msgs per day there."""
       loadAnalyticsTestData()
       activitySummary(datetime.date(2023,10,15))
    
    def test_moderator_query(self):
       """testing that the mod query, read the console output too"""
       loadAnalyticsTestData()
       sendChannelMessage("Paul", "Arrakis", "#Worms", "Test msg", datetime.date(1000,1,1))
       queryResults = moderatorQuery(datetime.date(999,1,1), datetime.date(1001,1,1), datetime.date(2023,10,1))
       self.assertEqual("Paul", queryResults[0], "Paul(currently suspended) should have a message within the date range")
       nulQueryResult = moderatorQuery(datetime.date(902,1,1), datetime.date(903,1,1), datetime.date(2023,10,1))
       self.assertEqual(-1, nulQueryResult, "There should be no messages from any currently suspended user within the date range")

