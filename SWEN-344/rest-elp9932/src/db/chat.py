from .swen344_db_utils import *

import datetime
import hashlib
import secrets

def rebuildTables():
    """Drops the main tables if they exist, and creates fresh ones"""
    exec_commit("""
        DROP TABLE IF EXISTS membership_list;
        DROP TABLE IF EXISTS unread_list;
        DROP TABLE IF EXISTS suspension_list;
        DROP TABLE IF EXISTS message_list;
        DROP TABLE IF EXISTS channel_list;
        DROP TABLE IF EXISTS community_list;
        DROP TABLE IF EXISTS user_list;
    """)
    exec_sql_file('src/db/build_user_table.sql')
    exec_sql_file('src/db/build_community_table.sql')
    exec_sql_file('src/db/build_channel_table.sql')
    exec_sql_file('src/db/build_message_table.sql')
    exec_sql_file('src/db/build_suspension_table.sql')
    exec_sql_file('src/db/build_unread_table.sql')
    exec_sql_file('src/db/build_membership_table.sql')

def buildFullTestData():
    exec_sql_file('src/db/user_data.sql')
    exec_sql_file('src/db/community_data.sql')
    exec_sql_file('src/db/message_data.sql') #unsure where to throw the test data. here seemed like a good spot.
    loadAnalyticsTestData()

def loadNewTestData():
   """Builds the initial test data with the additional data for DB2 testing"""
   createUser("DrMarvin", "790-575-3487", "pass6")
   createUser("Bob", "683-287-2535", "pass7")
   sendDM("Bob", "DrMarvin", "I’m doing the work, I’m baby-stepping", datetime.date(1991, 5, 18))

def loadCommunityTestData():
   """Builds the initial test data with the community augmentations for DB3 testing"""
   loadNewTestData()
   createUser("spicelover", "645-204-1958", "pass8")
   joinCommunity("spicelover", "Arrakis")
   createUser("Paul", "650-186-2739", "pass9") #"verify that the db is correectly updated after each action" I think ive confirmed that user creation works enough. I'm not verifying this. 
   joinCommunity("Paul", "Arrakis") #verified in test_community_join. Up here because I frequently need it for the db3 tests
   sendDM("Paul", "Moe", "test message P to M", None)
   sendDM("Moe", "Paul", "test message M to P", None)
   sendDM("Paul", "Moe", "test message P to M #2", None)

def loadAnalyticsTestData():
   loadCommunityTestData()
   suspendUser("Paul", "Arrakis", datetime.date(2222, 2, 22), datetime.date(2023, 10, 1))
   sendChannelMessage("Abbott", "Comedy", "#Dialogs", "please reply", None)
   sendChannelMessage("Costello", "Comedy", "#Dialogs", "i replied already!", None)
   sendDM("Paul", "Moe", "the word reply", None)

def getIDFromUsername(username):
    """Takes in a username as a string and returns the internal ID for that user
    Returns -1 on a bad username, otherwise returns the id of the username in question

    Keyword arguments:
    username -- Username of the user to get the id for as a string. Assumed to be valid
    """
    result = exec_get_one(f'select ID FROM user_list WHERE username=\'{username}\'')
    if result == None: return -1
    else: return result[0]

def getIDFromCommunityName(community):
    """Takes in a community name as a string and returns the internal ID for that community
    Returns -1 on a bad community name, otherwise returns the id of the community in question

    Keyword arguments:
    community -- Community name of the community to get the id for as a string. Assumed to be valid
    """
    result = exec_get_one(f'select communityID FROM community_list WHERE communityName=\'{community}\'')
    if result == None: return -1
    else: return result[0]

def getIDFromChannelWithCommunity(channel, community):
    """Takes in a community name and a channel name as strings and returns the internal ID for that channel
    Returns -1 on a bad name for either param, otherwise returns the id of the channel in question

    This method of searching should allow for 2 channels in different communities with identical names

    Keyword arguments:
    community -- Community name of the community to get the id for as a string. Assumed to be valid
    """
    result = exec_get_one(f'select channelID FROM channel_list WHERE (communityID=\'{getIDFromCommunityName(community)}\' AND channelName=\'{channel}\')')
    if result == None: return -1
    else: return result[0]

def getCommunityIDfromChannelID(channelID):
    """Takes in a channel id and returns the internal ID for the community it belongs to
    Returns -1 on a bad channel id, otherwise returns the id of the community in question

    Keyword arguments:
    channelID -- Channel id tied to the community to get the id for. Assumed to be valid
    """
    result = exec_get_one(f'select communityID FROM channel_list WHERE channelID=\'{channelID}\'')
    if result == None: return -1
    else: return result[0]

def getCommunityIDsFromMember(username):
    """Takes in a username and returns the internal ids for every community that member belongs to
    Returns -1 on a bad username, otherwise returns every community id in a list

    Keyword arguments:
    username -- user to get the community id list for
    """
    result = exec_get_all(f'select communityID FROM membership_list WHERE userID=\'{getIDFromUsername(username)}\'')
    if result == None: return -1
    else: return result

def getChannelIDsFromCommunityID(communityID):
    """Takes in a community and returns the internal ids for every channel that community has
    Returns -1 on a bad communityID, otherwise returns every channel id in a list

    Keyword arguments:
    communityID -- community to get the channel id list for
    """
    result = exec_get_all(f'select channelID FROM channel_list WHERE communityID=\'{communityID}\'')
    if result == None: return -1
    else: return result

def getMembersOfCommunity(community):
    """Takes in a community and returns the userIDs for every member in that community
    Returns -1 on a bad communityID, otherwise returns every user id in a list

    Keyword arguments:
    community -- community to get the user id list for
    """
    return exec_get_all(f"""SELECT userID FROM membership_list WHERE communityID=\'{getIDFromCommunityName(community)}\'""")

def getAllCommunities():
    """Returns a list of all valid community ids"""
    return exec_get_all(f"SELECT communityID FROM community_list")

def getSuspendedUsers(date):
    """Returns a list of the userids for all currently suspended users"""
    return exec_get_all(f"SELECT userID FROM suspension_list WHERE (suspensionStart <= \'{date}\' AND \'{date}\' < suspensionEnd)")

def isUserBanned(username, community, date):
    """Takes in a username, a community and a date and checks if the user is banned during that time in that community
    Returns true or false if that user is banned during the given date in the given community

    Keyword arguments:
    username -- the user to search for. takes in their username as a string, assumed to be valid
    community -- the community to check for the user's suspension. Assumed to be valid.
    date -- the date in datetime.date format to check if the user is banned during    
    """
    result = exec_get_one(f'select suspensionStart, suspensionEnd FROM suspension_list WHERE (userID=\'{getIDFromUsername(username)}\' AND communityID=\'{getIDFromCommunityName(community)}\')')
    if result == None: return False
    if result[0] == None: return False
    else: 
        return result[0]<=date<result[1]

def getDMs(sender, recipient):
    """Gets all DMs sent from the sender to the recipient(does not go both ways and get DMs from recipient to sender)
    Returns all DMs from the given users in question(one way only)

    Keyword arguments:
    sender -- username of user who sent the DM
    recipient -- username of user who recieved the DM 
    """
    result = exec_get_all(f'select * FROM message_list WHERE (senderID=\'{getIDFromUsername(sender)}\' AND recipientID=\'{getIDFromUsername(recipient)}\')')
    return result

def getMessagesInChannel(community, channel):
    """Gets all messages sent in a channel
    Returns a list of all messages in a given channel and community

    Keyword arguments:
    community -- the community the messages were sent in
    channel -- the channel the messages were sent in 
    """
    result = exec_get_all(f'select * FROM message_list WHERE (channelID=\'{getIDFromChannelWithCommunity(channel, community)}\')')
    return result

def getMessagesInChannelFromUser(community, channel, username):
    """Gets all messages sent in a channel from a given user in a specific community
    Returns a list of all messages in a given channel in a specific community from a given user 

    Keyword arguments:
    community -- the community the messages were sent in
    channel -- the channel the messages were sent in  
    username -- user the messages were from
    """
    result = exec_get_all(f'select * FROM message_list WHERE (channelID=\'{getIDFromChannelWithCommunity(channel, community)}\' AND senderID=\'{getIDFromUsername(username)}\')')
    return result

def getDMsInDateRange(sender, recipient, startDate, endDate): #dates should be in datetime.date format
    """Gets all DMs from the sender to the recipient during the specified date range. Same restrictions as getDMs
    Returns all DMs from the given users in question(one way only) during the date range

    Keyword arguments:
    sender -- username of user who sent the DM
    recipient -- username of user who recieved the DM  
    startDate -- start of the date range to search in datetime.date format
    endDate -- end of the date range to search in datetime.date format   
    """
    result = exec_get_all(f'select * FROM message_list WHERE (senderID=\'{getIDFromUsername(sender)}\' AND recipientID=\'{getIDFromUsername(recipient)}\' AND \'{startDate}\'<=date AND date<=\'{endDate}\')')
    return result

def getUnreadMessages(recipient):
    """Gets and returns all unread messages for a specific recipient(as a result could include different senders or channels)

    Keyword arguments: 
    recipient -- username of the person who has unread messages sent from other users    
    """
    return exec_get_all(f'select * FROM message_list WHERE msgID in (SELECT messageID FROM unread_list WHERE userID=\'{getIDFromUsername(recipient)}\')')

def createUser(username, phoneNum, password):
    """Creates a new user with the given username and phone number, returns 1 on success and -1 if the username is already in use
    
    Keyword arguments:
    username -- username to assign to the new user
    phoneNum -- phone number to assign to the new user
    password -- password to assign to the new user
    """

    encryption = hashlib.sha512() #encrypt password
    encryption.update(bytes(password, 'utf-8'))
    resultPass = encryption.hexdigest()
    if (getIDFromUsername(username) != -1): return -1 
    exec_commit(f"""INSERT INTO user_list VALUES (DEFAULT, \'{username}\', \'{phoneNum}\', DEFAULT, \'{resultPass}\', DEFAULT);""")
    return 1

def sendDM(sender, recipient, content, timestamp):
    """Sends a new message from a sender to a recipient at a certain date
    Date is optional, defaults to current timestamp
    
    Gives the recipient one unread message from the sender as well. 

    Keyword arguments:
    sender -- user who the message comes from
    recipient -- user who the message goes to
    content -- the actual message being sent 
    timestamp -- optional, what the timestamp is on message send, defaults to current date/time if None as an argument     
    """


    if(timestamp == None):
        exec_commit(f"""
             INSERT INTO message_list
             VALUES 
             (DEFAULT, \'{getIDFromUsername(sender)}\', \'{getIDFromUsername(recipient)}\', DEFAULT, $${content}$$, DEFAULT);
        """)
    else: 
        exec_commit(f"""
             INSERT INTO message_list
             VALUES 
             (DEFAULT, \'{getIDFromUsername(sender)}\', \'{getIDFromUsername(recipient)}\', DEFAULT, $${content}$$, \'{timestamp}\');
        """)
    exec_commit(f""" 
         INSERT INTO unread_list
         VALUES
         (DEFAULT, \'{getIDFromUsername(recipient)}\', (SELECT MAX(msgID) FROM message_list))
    """) #latest message should have the highest primary key by default
    return 1

def sendChannelMessage(sender, community, channel, content, timestamp):
    """Sends a new message from a sender to a channel in a community at a certain date
    Date is optional, defaults to current timestamp. Returns the suspension end date if the user is suspended in the community at the given timestamp
    
    Gives anyone in the channel that isn't the sender one unread message from the channel/community as well. 

    Keyword arguments:
    sender -- user who the message comes from
    community -- community the message goes to
    channel -- channel the message gets sent in 
    content -- the actual message being sent 
    timestamp -- optional, what the timestamp is on message send, defaults to current date/time if None as an argument     
    """
    if(timestamp == None):
        banTime = datetime.date.today()
    else:
        banTime = timestamp

    if isUserBanned(sender, community, banTime):
        result = exec_get_one(f'select suspensionEnd FROM suspension_list WHERE userID=\'{getIDFromUsername(sender)}\'')
        return result[0]

    if(timestamp == None):
        exec_commit(f"""
             INSERT INTO message_list
             VALUES 
             (DEFAULT, \'{getIDFromUsername(sender)}\', DEFAULT, \'{getIDFromChannelWithCommunity(channel, community)}\', $${content}$$, DEFAULT)
        """)
    else: 
        exec_commit(f"""
             INSERT INTO message_list
             VALUES 
             (DEFAULT, \'{getIDFromUsername(sender)}\', DEFAULT, \'{getIDFromChannelWithCommunity(channel, community)}\', $${content}$$, \'{timestamp}\')
        """)

    usersToUnread = getMembersOfCommunity(community)
    for userID in usersToUnread:
         if(userID[0] != getIDFromUsername(sender)):
             exec_commit(f""" 
                 INSERT INTO unread_list
                 VALUES
                 (DEFAULT, \'{userID[0]}\', (SELECT MAX(msgID) FROM message_list))
             """)
    return 1

def readDMConversation(user1, user2):
    """gets and returns all the messages back and forth between two users, sorted by timestamps
    
    Keyword arguments:
    user1 -- first user
    user2 -- second user
    """
    messages = getDMs(user1, user2)
    messages = messages + getDMs(user2, user1)
    messages.sort(key=lambda x: x[5])
    return messages

def getDetailedUnreadDMs(recipient):
    """Creates and returns a dictionary of the userIDs of senders as the key and any unread messages sent from them to the given recipient recipient in an array as the value
    
    Keyword arguments:
    recipient -- the recipient of the messages
    """
    senderList = getUnreadMessages(recipient) # take unreads, filter by dms
    dmList = []
    for message in senderList:
        if(message[1] != None):
            dmList.append(message)
    detailList = {}
    for message in dmList: #dictionary sorting into output format
        if(detailList.get(message[1], None) == None):
            detailList[message[1]] = [message]
        else:
            detailList[message[1]] = detailList.get(message[1]).append(message)
    return detailList


def getDetailedUnreadChannels(recipient, community):
    """Creates and returns a dictionary of the channel ids as the key and any unread messages sent in them to a channel that the user hasnt read in an array as the value.
    Works on a per community basis and only returns for one community at a time.

    Keyword arguments:
    recipient -- user who has unread messages
    community -- the community the unread messages were sent in
    """
    senderList = getUnreadMessages(recipient) # take unreads, filter by non dms
    commMessageList = []
    for message in senderList:
        if(message[3] != None):
            commMessageList.append(message)
    detailList = {}
    for message in commMessageList: # filters by channel and removes the non important communities
        if(getCommunityIDfromChannelID(message[3]) == getIDFromCommunityName(community)):
            if(detailList.get(message[3], None) == None):
                detailList[message[3]] = [message]
            else:
                detailList[message[3]] = detailList.get(message[3]).append(message)
    return detailList

def getDetailedUnreadCommunities(recipient):
    """Creates and returns a dictionary of the community ids as the key and any unread messages sent in them that the user hasnt read in an array as the value.

    Note that messages themselves have a channel id foreign key field. 

    Keyword arguments:
    recipient -- user who has unread messages
    community -- the community the unread messages were sent in
    """
    senderList = getUnreadMessages(recipient) # take unreads, filter by non dms
    commMessageList = []
    for message in senderList:
        if(message[3] != None):
            commMessageList.append(message)
    detailList = {}
    for message in commMessageList: #filter by community
            commID = getCommunityIDfromChannelID(message[3])
            if(detailList.get(commID, None) == None):
                detailList[commID] = [message]
            else:
                detailList[commID] = detailList.get(commID).append(message)
    return detailList

def markAsRead(messageID):
    """marks a message as read by removing it from the unread_list table
    
    Keyword arguments:
    messageID -- the id of the message to mark as read
    """
    exec_commit(f"""
         DELETE FROM unread_list
         WHERE messageID = \'{messageID}\';
    """)

def updateUsername(oldUsername, newUsername, timestamp):
    """updates a username to a new one, with a given timestamp of when the update is taking place. returns -1 if new username is already in use or the six month change limit is currently in effect

    Keyword arguments:
    oldUsername -- the original user's username
    newUsername -- new username
    timestamp -- the datetime.date that this change is taking place on   
    """
    validChangeDate = exec_get_one(f'SELECT usernameChangeDate FROM user_list WHERE ID=\'{getIDFromUsername(oldUsername)}\'')
    if(validChangeDate[0] != None):
        if(validChangeDate[0] > timestamp):
            return -1
    if(getIDFromUsername(newUsername) != -1):
        return -1
    updateDate = timestamp + datetime.timedelta(days=183)
    exec_commit(f"""
         UPDATE user_list
         SET username = \'{newUsername}\', usernameChangeDate = \'{updateDate}\'
         WHERE ID = \'{getIDFromUsername(oldUsername)}\';
    """)
    return 1

def suspendUser(username, community, enddate, timestamp):
    """suspends a user for the given date range from the timestamp to the end date in a given community
    
    Keyword arguments:
    community -- the community the suspension will take place in
    username -- the user to suspend
    enddate -- datetime.date to end the suspension
    timestamp -- time this action happens at(suspension start date)
    """
    exec_commit(f"""
         INSERT INTO suspension_list 
         VALUES
         (DEFAULT, \'{getIDFromUsername(username)}\', \'{getIDFromCommunityName(community)}\', \'{timestamp}\', \'{enddate}\');
    """)


def removeSuspension(username, community):
    """removes a user's current suspension
    
    Keyword arguments:
    username -- the user to unsuspend
    community -- the community with the suspension to remove
    
    """
    exec_commit(f"""
         DELETE FROM suspension_list WHERE (userID=\'{getIDFromUsername(username)}\' AND communityID=\'{getIDFromCommunityName(community)}\');
    """)

def joinCommunity(username, community):
    """Makes a user a member of a comunity
    
    Keyword arguments:
    username -- the user to join a community
    community -- the community to join
    """
    exec_commit(f"""
         INSERT INTO membership_list
         VALUES
         (DEFAULT, \'{getIDFromUsername(username)}\', \'{getIDFromCommunityName(community)}\')         
    """)

def getMentions(username):
    """Gets and returns a list of all messages that contain a mention of a username(format of '@username') in all communities the user is a member of
    
    Keyword arguments:
    username -- the user being mentioned
    """
    validCommunities = getCommunityIDsFromMember(username)
    validChannels = []

    for communityID in validCommunities:
        validChannels = validChannels + getChannelIDsFromCommunityID(communityID[0])

    mentionList = []
    for channelID in validChannels:
        resultList = exec_get_all(f"SELECT * FROM message_list WHERE (channelID=\'{channelID[0]}\' AND STRPOS(messageContent, \'@{username}\') > 0)")
        mentionList = mentionList + resultList

    return mentionList

def searchMsgStringInCommunity(community, searchTerm):
    """Searches for the input string in the message content from the given community and returns a list of messages that contain that string.

    Prints a result to the console. 
    
    Keyword arguments:
    community -- the name of the community to search
    searchTerm -- string to search for
    """
    stringFormat = ' '.join(searchTerm.split()) #cleans up whitespace garbage just in case, necessary for next part
    stringFormat = stringFormat.replace(" ", " & ") #changes whitespace to the postgres formatting for multiple words in search
    commID = getIDFromCommunityName(community)
    validChannelIDs = getChannelIDsFromCommunityID(commID)
    validMessages = []
    for channelID in validChannelIDs:
        validMessages = validMessages + exec_get_all(f"SELECT * FROM message_list WHERE (channelID = \'{channelID[0]}\' AND (to_tsvector(messageContent) @@ to_tsquery(\'{stringFormat}\')))")
    actualMessages = []
    if(validMessages == [] or validMessages == None):
        print(f"\nFound 0 messages matching \"{searchTerm}\"") 
        return -1
    for message in validMessages:
        actualMessages.append(message[4])
    print(f"\nFound {len(validMessages)} messages matching \"{searchTerm}\":")
    print(actualMessages)
    return validMessages

def activitySummary(date):
    """takes in a specific date and returns a summary of activity over the previous 30 days from the input date.
    
    returns a table with the following values: Community name, average message per day(counting messages longer than 5 chars), and number of users who have sent at least one message longer than 5 chars
    should include a row for every valid community. 

    Prints the result to the console as well. 

    Keyword arguments:
    date -- date to search up to 30 days prior for the summary data
    """
    communityList = getAllCommunities()
    results = []
    for communityID in communityList: #iterate over each community
        communityName = exec_get_one(f"SELECT communityName FROM community_list WHERE communityID = \'{communityID[0]}\'")
        channelList = getChannelIDsFromCommunityID(communityID[0])
        messageCount = 0
        userCount = []
        startDate = date - datetime.timedelta(days = 30) 
        for channelID in channelList: #iterate over each channel in said community
            messageCountingData = exec_get_one(f"SELECT COUNT(*) FROM message_list WHERE(channelID = \'{channelID[0]}\' AND CHAR_LENGTH(messageContent)>5 AND \'{startDate}\' < date AND date <= \'{date}\')")
            messageCount = messageCount + messageCountingData[0]

            userChannelData = exec_get_all(f"SELECT DISTINCT senderID FROM message_list WHERE(channelID = \'{channelID[0]}\' AND CHAR_LENGTH(messageContent)>5 AND \'{startDate}\' < date AND date <= \'{date}\')")
            if userChannelData != None:
                userCount = userCount + userChannelData #will have double counting. need to purge duplicates
        finalMsgAverage = messageCount/30 #get actual average per day
        finalUserCount = len(list(set(userCount))) #purge duplicate userids and get count of what remains
        dataDict = {'community': communityName[0], 'avg_num_messages': round(finalMsgAverage, 2), 'active_users':finalUserCount}
        results.append(dataDict)
    print("\nActivity Summary:")
    print(results)
    return results

def moderatorQuery(startDate, endDate, currentDate):
    """takes in a start date and an end date, checks if a currently suspended user(currently according to currentDate) has sent a message within the time range and returns all suspended users who have
    
    Prints the result to the console as well.

    Keyword arguments:
    startDate -- start of the date range to search
    endDate -- end of the date range to search
    currentDate -- what counts as current date for the purpose of "currently suspended"
    """
    suspendedUsers = getSuspendedUsers(currentDate)
    markedUsers = []
    for userID in suspendedUsers:
        numMsgs = exec_get_one(f"SELECT COUNT(*) FROM message_list WHERE (senderID=\'{userID[0]}\' AND \'{startDate}\'<=date AND date<=\'{endDate}\')")[0]
        if numMsgs>0:
            markedUsers.append(userID)
    markedUsernames = []
    for userID in markedUsers:
        markedUsernames.append(exec_get_one(f"SELECT username FROM user_list WHERE ID=\'{userID[0]}\'")[0])
    if markedUsernames == []:
        print(f"\nThere are no messages from currently suspended users within the given date range")
        return -1
    else:
        print(f"\nThe following currently suspended users have sent a message in the given date range: {markedUsernames}")
        return markedUsernames

def getAllUsers():
    """Nothing special. Just gets all users."""
    return exec_get_all(f"SELECT * FROM user_list")

def getCommunityData():
    """Nothing special. just getting some names for all the channels with their community attached"""
    #inner join my despised
    return exec_get_all(f"SELECT channel_list.channelName, community_list.communityName FROM (channel_list INNER JOIN community_list ON channel_list.communityID = community_list.communityID)")

def msgContentAndUserIDSearch(msgContent, msgSize, dateRangeStart, dateRangeEnd):
    """Nothing special. Just an augmented variant of a previous function that searches by search term but from a specific user now. 
    Arguments are optional so a funny query builder was made to make the functional query. 

    Keyword arguments:
    msgContent -- message string to search for. Optional.
    msgSize -- length of message in characters. Optional.
    dateRangeStart -- start range of a date to search. Optional.
    dateRangeEnd -- end range of a date to search. Optional.
    """

    sqlQuery = f"""SELECT sender.username, recipient.username, channel_list.channelName, community_list.communityName, message_list.messageContent, message_list.date 
                FROM ((((message_list INNER JOIN user_list sender ON message_list.senderID = sender.ID) 
                LEFT JOIN user_list recipient ON message_list.recipientID = recipient.ID)
                LEFT JOIN channel_list ON message_list.channelID = channel_list.channelID)
                LEFT JOIN community_list ON channel_list.communityID = community_list.communityID)"""
    # base query that runs if there are no params passed

    if((msgContent or msgSize or dateRangeStart or dateRangeEnd)!=None): #checks if any additional params are passed
        sqlQuery = sqlQuery + " WHERE (" #opens arguments

        if(msgContent!=None): #msg content param
            stringFormat = ' '.join(msgContent.split()) #cleans up whitespace garbage just in case, necessary for next part
            stringFormat = stringFormat.replace(" ", " & ") #changes whitespace to the postgres formatting for multiple words in search
            sqlQuery = sqlQuery + f"(to_tsvector(message_list.messageContent) @@ to_tsquery(\'{stringFormat}\'))"

        if(msgSize!=None): #msg size param
            if(msgContent!=None): sqlQuery = sqlQuery + " AND "
            sqlQuery = sqlQuery + f"(LENGTH(message_list.messageContent) = {msgSize})"

        if(dateRangeStart!=None): #date range start param
            formattedStart = datetime.datetime.strptime(dateRangeStart, '%m-%d-%Y').date()
            if((msgContent or msgSize)!=None): sqlQuery = sqlQuery + " AND "
            sqlQuery = sqlQuery + f"(\'{formattedStart}\'<=date)"

        if(dateRangeEnd!=None): #date range end param
            formattedEnd = datetime.datetime.strptime(dateRangeEnd, '%m-%d-%Y').date()
            if((msgContent or msgSize or dateRangeStart)!=None): sqlQuery = sqlQuery + " AND "
            sqlQuery = sqlQuery + f"(date<=\'{formattedEnd}\')"
        
        sqlQuery = sqlQuery + ")" #closes arguments
        #istg this has to have an easier solution that im not seeing. this builder works fine though. 
    return exec_get_all(sqlQuery)


def getMessagesInChannelID(channelID):
    """Another nothing special variant of an existing command. 
    Gets all messages sent in a channel
    Returns a list of all messages in a given channel in a more human readable format

    Keyword arguments:

    channelID -- the channel the messages were sent in 
    """
    result =exec_get_all(f"""SELECT sender.username, channel_list.channelName, community_list.communityName, message_list.messageContent, message_list.date 
                FROM (((message_list INNER JOIN user_list sender ON message_list.senderID = sender.ID) 
                LEFT JOIN channel_list ON message_list.channelID = channel_list.channelID)
                LEFT JOIN community_list ON channel_list.communityID = community_list.communityID)
                WHERE (message_list.channelID= \'{channelID}\')""")
    return result

def updatePhoneNum(username, phoneNum):
    """Simple command to update something that i havent had to bother with yet. 
    updates a users phone number, returns 1 on success and -1 if the user isnt valid

    Keyword arguments:

    username -- the users phone num to update
    phoneNum -- the new phone num
    """
    if (getIDFromUsername(username) == -1): return -1 
    exec_commit(f"""
         UPDATE user_list
         SET phoneNumber = \'{phoneNum}\'
         WHERE ID = \'{getIDFromUsername(username)}\';
    """)
    return 1

def deleteUser(username):
    """Suprised I hadnt made this yet. Deletes a user, and removes all their messages and memberships from the database

    Keyword arguments:
    username -- the user to delete
    """
    if (getIDFromUsername(username) == -1): return -1 
    exec_commit(f"DELETE FROM user_list WHERE ID = \'{getIDFromUsername(username)}\';")
    return 1

def login(username, password):
    """Here comes the fun stuff. Initiates the REST user login. Takes in a username and password. If the username or password is invalid returns -1.

    Password is stored in the db as a sha512 hash so it needs to be hashed and then compared. 
    If both are valid, creates a session key to pass to the client. Said session key is hashed via sha512 and stored in the database, and an unhashed copy is returned to the client for use. 

    Keyword arguments:
    username -- username login
    password -- password login
    """
    if (getIDFromUsername(username) == -1): return -1 #valid user

    passEncryption = hashlib.sha512() #encrypt password
    passEncryption.update(bytes(password, 'utf-8'))
    resultPass = passEncryption.hexdigest()
    userPass = exec_get_one(f"SELECT password FROM user_list WHERE ID = \'{getIDFromUsername(username)}\'")[0]
    if(userPass != resultPass): return -1 #validate password

    resultKey = secrets.token_urlsafe() #create session key
    keyEncryption = hashlib.sha512()
    keyEncryption.update(bytes(resultKey, 'utf-8'))

    exec_commit(f""" 
         UPDATE user_list
         SET session_key = \'{keyEncryption.hexdigest()}\'
         WHERE ID = \'{getIDFromUsername(username)}\';
    """) #store encrypted session key

    return resultKey #return unencrypted session key

def validateOperation(hashKey):
    """Validates the passed in session key with that user's one from the database. Returns the corresponding userid as an identifier to whos session key that is. 
    Returns -1 on invalid session key. 

    Keyword arguments:
    hashKey -- the client's unhashed copy of the session key
    """
    if hashKey == None: return -1

    keyEncryption = hashlib.sha512()
    keyEncryption.update(bytes(hashKey, 'utf-8'))

    validity = exec_get_one(f"SELECT ID FROM user_list WHERE session_key = \'{keyEncryption.hexdigest()}\'")
    if validity == None: return -1
    return validity[0]

def getUsernameFromID(id):
    """Suprised I hadnt made this yet. Reverse of the get id from username method. Takes id and returns string of username. Used to take the result of validateOperaton and run api commands to the db from the result. 

    Keyword arguments:
    id -- the user's id
    """
    result = exec_get_one(f'select username FROM user_list WHERE ID=\'{id}\'')
    if result == None: return -1
    else: return result[0]

def updatePassword(username, password):
    """Updates a user's password. New password gets rehashed and stored in the db. 
    Returns -1 on invalid user and 1 on success.

    Keyword arguments:
    username -- the user to update the password of
    password -- the new password
    """
    if (getIDFromUsername(username) == -1): return -1 

    encryption = hashlib.sha512() #encrypt password
    encryption.update(bytes(password, 'utf-8'))
    resultPass = encryption.hexdigest()

    exec_commit(f"""
         UPDATE user_list
         SET password = \'{resultPass}\'
         WHERE ID = \'{getIDFromUsername(username)}\';
    """)
    return 1

def logout(userID):
    """Counterpart to the login function. Takes in a userid and closes their session by deleting their session key. 

    Keyword arguments:
    userID -- the id of the user whos session is being deleted. 
    """
    exec_commit(f"""
         UPDATE user_list SET session_key = NULL
         WHERE ID = \'{userID}\';
    """)