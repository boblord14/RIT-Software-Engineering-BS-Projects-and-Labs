CREATE TABLE user_list(
    ID SERIAL PRIMARY KEY, 
    username varchar(32) NOT NULL,
    phoneNumber VARCHAR(12) NOT NULL,
    usernameChangeDate DATE DEFAULT NULL,
    password varchar(128) NOT NULL, 
    session_key varchar(128) DEFAULT NULL
)