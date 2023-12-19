CREATE TABLE unread_list(
    unreadID SERIAL PRIMARY KEY, 
    userID int NOT NULL references user_list(ID) ON DELETE CASCADE,
    messageID int NOT NULL references message_list(msgID) ON DELETE CASCADE
)