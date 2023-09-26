CREATE TABLE channel_list(
    channelID SERIAL PRIMARY KEY, 
    channelName varchar(32) NOT NULL,
    communityID int NOT NULL references community_list(communityID)
)