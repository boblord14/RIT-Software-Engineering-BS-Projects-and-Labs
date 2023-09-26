CREATE TABLE suspension_list(
    suspensionID SERIAL PRIMARY KEY, 
    userID int NOT NULL references user_list(ID),
    communityID int NOT NULL references community_list(communityID),
    suspensionStart DATE DEFAULT NULL,
    suspensionEnd DATE DEFAULT NULL
)