CREATE TABLE message_list(
    msgID SERIAL PRIMARY KEY, 
    senderID int NOT NULL references user_list(ID) ON DELETE CASCADE,
    recipientID int references user_list(ID) ON DELETE CASCADE DEFAULT NULL,
    channelID int references channel_list(channelID) DEFAULT NULL,
    messageContent VARCHAR(255) NOT NULL,
    date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    constraint oneDestination check ((recipientID is null or channelID is null) and not (recipientID is null and channelID is null) )
)