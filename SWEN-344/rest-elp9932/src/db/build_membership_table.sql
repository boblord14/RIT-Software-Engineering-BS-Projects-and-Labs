CREATE TABLE membership_list(
    membershipID SERIAL PRIMARY KEY, 
    userID int NOT NULL references user_list(ID) ON DELETE CASCADE,
    communityID int NOT NULL references community_list(communityID)
)