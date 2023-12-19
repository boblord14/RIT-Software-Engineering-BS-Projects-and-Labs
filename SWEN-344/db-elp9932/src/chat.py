from src.swen344_db_utils import *
import datetime

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
    exec_sql_file('/db-elp9932/src/build_user_table.sql')
    exec_sql_file('/db-elp9932/src/build_community_table.sql')
    exec_sql_file('/db-elp9932/src/build_channel_table.sql')
    exec_sql_file('/db-elp9932/src/build_message_table.sql')
    exec_sql_file('/db-elp9932/src/build_suspension_table.sql')
    exec_sql_file('/db-elp9932/src/build_unread_table.sql')
    exec_sql_file('/db-elp9932/src/build_membership_table.sql')

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

def createUser(username, phoneNum):
    """Creates a new user with the given username and phone number, returns 1 on success and -1 if the username is already in use
    
    Keyword arguments:
    username -- username to assign to the new user
    phoneNum -- phone number to assign to the new user
    """
    if (getIDFromUsername(username) != -1): return -1 
    exec_commit(f"""INSERT INTO user_list VALUES (DEFAULT, \'{username}\', \'{phoneNum}\', DEFAULT);""")
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


    
    