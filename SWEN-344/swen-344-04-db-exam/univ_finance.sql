DROP TABLE IF EXISTS CONTRACTS;
DROP TABLE IF EXISTS DEPT;
DROP TABLE IF EXISTS POSITION;
DROP TABLE IF EXISTS SALARIES;
DROP TABLE IF EXISTS FACULTY;

CREATE TABLE DEPT
(
	ID SERIAL PRIMARY KEY,
	NAME VARCHAR(100), -- Department full name
	CODE VARCHAR(10) -- Department code
);

INSERT INTO DEPT (NAME, CODE) VALUES('Software Engineering', 'GCS-3');
INSERT INTO DEPT (NAME, CODE) VALUES('Mathematics', 'A&S-2');
INSERT INTO DEPT (NAME, CODE) VALUES('Biology',  'A&S-5');

CREATE TABLE  POSITION 
   (	
	ID SERIAL PRIMARY KEY,
    TITLE VARCHAR(100) , 
	CODE VARCHAR(10)
	 );

INSERT INTO POSITION (TITLE, CODE) VALUES ('Associate Professor', 'P-2');
INSERT INTO POSITION (TITLE, CODE)  VALUES ('Assistant Professor', 'P-1');
INSERT INTO POSITION (TITLE, CODE)  VALUES ('Professor', 'P-3' );
INSERT INTO POSITION (TITLE, CODE)  VALUES ('Lecturer', 'L-1' );
INSERT INTO POSITION (TITLE, CODE)  VALUES ('Senior Lecturer', 'L-2');
INSERT INTO POSITION (TITLE, CODE)  VALUES ('Principal Lecturer', 'L-3');
INSERT INTO POSITION (TITLE, CODE)  VALUES ('Adjunct', 'T-1');


CREATE TABLE  FACULTY 
   (	
    ID SERIAL PRIMARY KEY, 
	FIRST_NAME VARCHAR(100), 
	LAST_NAME VARCHAR(100), 
	DEPT_ID INT, 
	POSITION_ID INT, 
	HIRE_DATE DATE -- The date they were first hired
);   

INSERT INTO FACULTY VALUES 
(0, 'Sherlock', 'Holmes', 1, 4, '04-10-1887'),
(1, 'John', 'Smith', 1, 1, '2020-01-15'),
(2, 'Emily', 'Johnson', 2, 3, '2018-09-22'),
(3, 'Michael', 'Williams', 1, 2, '2022-03-10'),
(4, 'Sarah', 'Davis', 3, 4, '2019-07-05'),
(5, 'David', 'Brown', 1, 3, '2021-11-30'),
(6, 'Jessica', 'Miller', 2, 1, '2017-04-18'),
(7, 'Daniel', 'Wilson', 3, 2, '2023-02-08'),
(8, 'Melissa', 'Martinez', 1, 1, '2018-06-20'),
(9, 'Andrew', 'Anderson', 2, 3, '2016-09-12'),
(10, 'Laura', 'Thompson', 1, 4, '2020-08-14'),
(11, 'Christopher', 'Hall', 3, 2, '2019-03-25'),
(12, 'Rachel', 'White', 2, 1, '2022-01-03'),
(13, 'Kevin', 'Lewis', 1, 3, '2017-12-09'),
(14, 'Amanda', 'Harris', 3, 4, '2021-05-19'),
(15, 'Nicholas', 'Clark', 1, 2, '2018-10-28'),
(16, 'Stephanie', 'Allen', 2, 1, '2019-09-17'),
(17, 'Justin', 'Turner', 1, 3, '2023-06-02'),
(18, 'Olivia', 'Young', 3, 2, '2020-04-12'),
(19, 'William', 'Rodriguez', 2, 1, '2017-02-05'),
(20, 'Nicole', 'King', 1, 4, '2022-07-30'),
(21, 'Jason', 'Brooks', 3, 2, '2018-11-23'),
(22, 'Erica', 'Scott', 2, 1, '2016-08-09'),
(23, 'Brian', 'Green', 1, 3, '2021-03-17'),
(24, 'Megan', 'Bailey', 3, 4, '2017-07-11'),
(25, 'Zachary', 'Murphy', 2, 2, '2019-01-28');

ALTER SEQUENCE FACULTY_ID_SEQ RESTART WITH 26;

CREATE TABLE  SALARIES 
   (
    ID SERIAL PRIMARY KEY, 
	FACULTY_ID INTEGER NOT NULL REFERENCES FACULTY, --FOREIGN KEY FOR FACULTY ITEM
	SALARY DECIMAL(12,2) NOT NULL, --Current Salary
	EFFECTIVE_DATE DATE NOT NULL --Effective date of the current salary
   );

INSERT INTO Salaries (ID, FACULTY_ID, SALARY, EFFECTIVE_DATE) VALUES
(1, 1, 80000, '2020-01-01'),
(2, 2, 60000, '2021-05-01'),
(3, 3, 70000, '2022-01-01'),
(4, 4, 55000, '2019-07-01'),
(5, 5, 65000, '2021-01-01'),
(6, 6, 90000, '2017-04-01'),
(7, 7, 75000, '2023-02-01'),
(8, 8, 82000, '2018-06-01'),
(9, 9, 60000, '2016-09-01'),
(10, 10, 50000, '2020-08-01'),
(11, 11, 68000, '2019-03-01'),
(12, 12, 92000, '2022-01-01'),
(13, 13, 65000, '2017-12-01'),
(14, 14, 55000, '2021-05-01'),
(15, 15, 72000, '2018-10-01'),
(16, 16, 88000, '2019-09-01'),
(17, 17, 61000, '2023-06-01'),
(18, 18, 69000, '2020-04-01'),
(19, 19, 92000, '2017-02-01'),
(20, 20, 48000, '2022-07-01'),
(21, 21, 67000, '2018-11-01'),
(22, 22, 91000, '2016-08-01'),
(23, 23, 63000, '2021-03-01'),
(24, 24, 52000, '2017-07-01'),
(25, 25, 74000, '2019-01-01'),
(26, 0, 2000, '1887-04-10');

ALTER SEQUENCE SALARIES_ID_SEQ RESTART WITH 27;

CREATE TABLE CONTRACTS
(
	ID SERIAL PRIMARY KEY,
	FACULTY_ID INT,
	TYPE VARCHAR(20),
	START_DATE DATE, -- Start date of the current contract
	END_DATE DATE -- End date of the contract.  If END_DATE IS NULL, then they are still active/ working
);

-- Inserting data into the Contracts table; adjuncts are always part-time 
INSERT INTO Contracts (ID, FACULTY_ID, TYPE, START_DATE, END_DATE) VALUES
(1, 1, 'Full-Time', '2020-01-15', NULL),
(2, 2, 'Full-Time', '2018-09-22', NULL), 
(3, 3, 'Part-Time', '2022-03-10', '2023-03-10'),
(4, 4, 'Full-Time', '2019-07-05', NULL),
(5, 5, 'Full-Time', '2021-11-30', NULL),
(6, 6, 'Full-Time', '2017-04-18', NULL),
(7, 7, 'Full-Time', '2023-02-08', NULL),
(8, 8, 'Full-Time', '2018-06-20', NULL),
(9, 9, 'Full-Time', '2016-09-12', NULL),
(10, 10, 'Part-Time', '2020-08-14', NULL),
(11, 11, 'Full-Time', '2019-03-25', NULL),
(12, 12, 'Full-Time', '2022-01-03', NULL),
(13, 13, 'Full-Time', '2017-12-09', NULL),
(14, 14, 'Full-Time', '2021-05-19', NULL),
(15, 15, 'Full-Time', '2018-10-28', NULL),
(16, 16, 'Full-Time', '2019-09-17', NULL),
(17, 17, 'Full-Time', '2023-06-02', NULL),
(18, 18, 'Full-Time', '2020-04-12', NULL),
(19, 19, 'Full-Time', '2017-02-05', NULL),
(20, 20, 'Part-Time', '2022-07-30', NULL),
(21, 21, 'Full-Time', '2018-11-23', NULL),
(22, 22, 'Full-Time', '2016-08-09', NULL),
(23, 23, 'Full-Time', '2021-03-17', NULL),
(24, 24, 'Full-Time', '2017-07-11', NULL),
(25, 25, 'Full-Time', '2019-01-28', NULL),
(26, 0, 'Part-Time', '04-10-1887', NULL);

ALTER SEQUENCE CONTRACTS_ID_SEQ RESTART WITH 27;
