DROP TABLE IF EXISTS INVENTORY;
DROP TABLE IF EXISTS MANUFACTURER ;
DROP TABLE IF EXISTS EV_TYPE;
-- Table: AUTO Manufacturer
CREATE TABLE MANUFACTURER (
    ID SERIAL PRIMARY KEY NOT NULL, 
    NAME VARCHAR(256) NOT NULL, 
    HQ VARCHAR NOT NULL);

INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (1, 'Tesla', 'US');
INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (2, 'Toyota', 'Japan');
INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (3, 'General Motors', 'US');
INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (4, 'Volkwagen Group', 'Germany');
INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (5, 'Lucid Motors', 'US');
INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (6, 'Ford', 'US');
INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (7, 'Honda', 'Japan');
INSERT INTO MANUFACTURER (ID, Name, HQ) VALUES (8, 'BMW', 'Germany');

ALTER SEQUENCE MANUFACTURER_ID_SEQ RESTART WITH 9;


CREATE TABLE EV_TYPE(
    ID SERIAL PRIMARY KEY,
    TYPE VARCHAR(50)
);

INSERT INTO EV_TYPE (TYPE) VALUES 
('EV'),
('HYBRID'),
('PLUG-IN HYBRID');

-- Table: Dealer Inventory.  Covers a lot of brands
CREATE TABLE INVENTORY (
    ID SERIAL PRIMARY KEY NOT NULL, 
    MANF_ID INTEGER NOT NULL REFERENCES MANUFACTURER (ID), 
    MODEL VARCHAR(256) NOT NULL,
    TYPE_ID INT, 
    DESCRIPTION VARCHAR(1024),
    QUANTITY INTEGER
    );

INSERT INTO INVENTORY ( MANF_ID, MODEL, TYPE_ID, DESCRIPTION, QUANTITY) VALUES 
( 1, 'S', 1,'400 mile range and pricey!', 2),
( 1, '3', 1,'270 mile range and well, you know ... bring the benjamins!', 2),
( 1, 'Y', 1,'330 mile range and not so cheap!', 1),
( 2, 'Prius Prime', 3, 'Good starter vehicle!', 7),
( 2, 'Prius', 2, 'The original -- great combined mileage.  Used cars only', 7),
( 2, 'RAV-4 Prime', 3,'Electrical with plenty of leg-room',5),
( 3, 'Hummer EV Pickup', 1,'Wait ... a Hummer EV?  Is that possible?', 1),
( 3, 'Cruise Origin', 1,'The GM Driveless vehicle!', 2),
( 3, 'Chevy Volt', 2,'This is what started it for GM -- used models only!!', 2),
( 3, 'Chevy Bolt', 3,'And now GM is all in on EV', 5),
( 4, 'ID.4', 1,'Still working on our naming strategy ... but it is all electric', 1),
( 5, 'Air', 1,'Yep - Lucid Air.  We are new and cool.', 4),
( 6, 'Mustang Mach-E', 1,'Oh yeah ... it''s a Mustang AND it''s electric!', 4),
( 6, 'F-150 Lightning', 1,'That sucker is quick!', 3),
( 7, 'Accord', 2, 'Basic sedan -- but helps the environment', 5)

