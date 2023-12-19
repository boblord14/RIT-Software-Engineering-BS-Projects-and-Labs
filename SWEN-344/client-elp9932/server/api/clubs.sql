DROP table if EXISTS clubs CASCADE;

CREATE TABLE clubs (
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL,
    count INTEGER NOT NULL DEFAULT 0,
    yellow INTEGER NOT NULL DEFAULT 80,
    max INTEGER NOT NULL DEFAULT 100,
    city varchar(30) NOT NULL,
    music varchar(30) NOT NULL
);

INSERT INTO clubs	
        VALUES 
        (DEFAULT, 'Club Arcane', DEFAULT, 70, 100, 'Tampa Bay', 'Power Metal'),
        (DEFAULT, 'Club Underground', DEFAULT, 30, 50, 'Dundee', 'Symphonic Metal'),
        (DEFAULT, 'Club Soda', DEFAULT, 12, 20, 'Antartica', 'Melodic Metal'),
        (DEFAULT, 'Studio 52', DEFAULT, 32, 52, 'Principality of Sealand', 'Speed Metal');



