from src.swen344_db_utils import *
import datetime
import decimal

def rebuildTables():
    exec_sql_file('./univ_finance.sql')

def getPosIdFromName(name):
    """Given a position name get the position id for that name. name assumed to be a valid entry"""
    posID = exec_get_one(f"SELECT ID FROM POSITION WHERE TITLE=\'{name}\'")
    return posID[0]

def getDeptIdFromName(name):
    """Given a department name get the dept id for that name. name assumed to be a valid entry"""
    deptId = exec_get_one(f"SELECT ID FROM DEPT WHERE NAME=\'{name}\'")
    return deptId[0]

def getFacultyIDFromName(firstName, lastName):
    """Given a person's name that is a valid faculty member, get the facultyID for their name"""
    facultyID = exec_get_one(f"SELECT ID FROM FACULTY WHERE (FIRST_NAME=\'{firstName}\' AND LAST_NAME=\'{lastName}\')")
    return facultyID[0]
def getAllFaculty():
    """Gets and returns all faculty, with their department, first/last names, title, salary, hire date, and contract start/end dates.
    Sorts by dept and then hire date, and prints result to console.
    """
    facultyData = exec_get_all("""
        SELECT DEPT.NAME, FACULTY.LAST_NAME, FACULTY.FIRST_NAME, POSITION.TITLE, SALARIES.SALARY, FACULTY.HIRE_DATE, CONTRACTS.START_DATE, CONTRACTS.END_DATE FROM 
        ((((FACULTY INNER JOIN DEPT ON FACULTY.DEPT_ID = DEPT.ID)
        INNER JOIN POSITION ON FACULTY.POSITION_ID = POSITION.ID)
        INNER JOIN SALARIES ON SALARIES.FACULTY_ID = FACULTY.ID)
        INNER JOIN CONTRACTS ON CONTRACTS.FACULTY_ID = FACULTY.ID)
        ORDER BY DEPT.NAME ASC, FACULTY.HIRE_DATE ASC
    """) #this is all the data we need, all sorted. now just need to format it 
    print("Format: Department, Last Name, First Name, Title, Salary, Hire Date, Contract Start, Contract End")
    for facultyEntry in facultyData:
        print(f"{facultyEntry[0]},{facultyEntry[1]},{facultyEntry[2]},{facultyEntry[3]},{str(facultyEntry[4])},{str(facultyEntry[5])},{str(facultyEntry[6])},{str(facultyEntry[7])},")
        #little bit of a lengthy print statement but needed to cast the decimal and datetime objects to string outputs

def newHire(firstName, lastName, dept, position, hireDate, salary):
    """Hires a new faculty. Takes in necessary fields, and creates them in the system. 
    As it's a new hire, its assumed their contract begins on their hire date. 

    Keyword arguments:
    firstName -- hire's first name
    lastName -- hire's last name
    dept -- department the hire joins
    position -- position the hire has
    hireDate -- date of the hire in datetime.date format
    salary -- starting salary of the hire
    """
    exec_commit(f"INSERT INTO FACULTY VALUES(DEFAULT, \'{firstName}\', \'{lastName}\', {getDeptIdFromName(dept)}, {getPosIdFromName(position)}, \'{hireDate}\')") 
    #seperate because it needs to generate the primary key before i can grab it for a fkey
    exec_commit(f"INSERT INTO SALARIES VALUES(DEFAULT, {getFacultyIDFromName(firstName, lastName)}, {salary}, \'{hireDate}\')")
    
    if(position=='Adjunct'):#most efficient way to check this and differentiate contract entries
        jobType= 'Part-Time'
    else:
        jobType= 'Full-Time'
    exec_commit(f"INSERT INTO CONTRACTS VALUES(DEFAULT, {getFacultyIDFromName(firstName, lastName)}, \'{jobType}\', \'{hireDate}\', NULL)")

def updateContractEnd(firstName, lastName, newEndDate):
    """Updates the contract of an existing faculty member to a new end date.

    Keyword arguments:
    firstName -- faculty's first name
    lastName -- faculty's last name
    newEndDate -- the new end date for the contract in datetime.date format
    """
    exec_commit(f"UPDATE CONTRACTS SET END_DATE = \'{newEndDate}\' WHERE FACULTY_ID = {getFacultyIDFromName(firstName, lastName)}")

def displayAllContracts():
    """Displays all contracts with their contractees in the console"""
    contractData = exec_get_all(f"""
        SELECT FACULTY.FIRST_NAME, FACULTY.LAST_NAME, CONTRACTS.TYPE, CONTRACTS.START_DATE, CONTRACTS.END_DATE FROM
        CONTRACTS INNER JOIN FACULTY ON CONTRACTS.FACULTY_ID = FACULTY.ID 
        ORDER BY CONTRACTS.START_DATE ASC
    """)
    print("Format: Contractee, Contract Type, Start Date, End Date")
    for contractEntry in contractData:
        print(f"{contractEntry[0]} {contractEntry[1]},{contractEntry[2]},{str(contractEntry[3])},{str(contractEntry[4])},")

def getAllHiresAfterYear(year):
    """Gets and prints out all hires after a certain year.
    If the year in question was 2021, all hires from 2022-01-01 and onwards would be returned.
    
    Keyword arguments:
    year -- the cutoff for the hiring date to return
    """
    facultyData = exec_get_all(f"""
        SELECT DEPT.NAME, FACULTY.LAST_NAME, FACULTY.FIRST_NAME, POSITION.TITLE, SALARIES.SALARY, FACULTY.HIRE_DATE, CONTRACTS.START_DATE, CONTRACTS.END_DATE FROM 
        ((((FACULTY INNER JOIN DEPT ON FACULTY.DEPT_ID = DEPT.ID)
        INNER JOIN POSITION ON FACULTY.POSITION_ID = POSITION.ID)
        INNER JOIN SALARIES ON SALARIES.FACULTY_ID = FACULTY.ID)
        INNER JOIN CONTRACTS ON CONTRACTS.FACULTY_ID = FACULTY.ID)
        WHERE FACULTY.HIRE_DATE > \'{datetime.date(year, 12,31)}\'
        ORDER BY DEPT.NAME ASC, FACULTY.HIRE_DATE ASC
    """) #copypaste from question 1 with a WHERE clause added to it. Is it really this easy?
    print("Format: Department, Last Name, First Name, Title, Salary, Hire Date, Contract Start, Contract End")
    for facultyEntry in facultyData:
        print(f"{facultyEntry[0]},{facultyEntry[1]},{facultyEntry[2]},{facultyEntry[3]},{str(facultyEntry[4])},{str(facultyEntry[5])},{str(facultyEntry[6])},{str(facultyEntry[7])},")
        #said for output to be identical to q1's. Well heres the exact print statement from q1 too. 

def getLatestHire():
    latestHire = exec_get_one(f"""
        SELECT FACULTY.LAST_NAME, FACULTY.FIRST_NAME, POSITION.TITLE, FACULTY.HIRE_DATE, DEPT.NAME FROM
        ((FACULTY INNER JOIN DEPT ON FACULTY.DEPT_ID = DEPT.ID)
        INNER JOIN POSITION ON FACULTY.POSITION_ID = POSITION.ID)
        ORDER BY FACULTY.HIRE_DATE DESC
    """)
    print(f"{latestHire[0]},{latestHire[1]},{latestHire[2]},{str(latestHire[3])},{latestHire[4]}")

def getActiveFaculty():
    facultyData = exec_get_all("""
        SELECT DEPT.NAME, FACULTY.LAST_NAME, FACULTY.FIRST_NAME, POSITION.TITLE, SALARIES.SALARY, FACULTY.HIRE_DATE, CONTRACTS.START_DATE, CONTRACTS.END_DATE FROM 
        ((((FACULTY INNER JOIN DEPT ON FACULTY.DEPT_ID = DEPT.ID)
        INNER JOIN POSITION ON FACULTY.POSITION_ID = POSITION.ID)
        INNER JOIN SALARIES ON SALARIES.FACULTY_ID = FACULTY.ID)
        INNER JOIN CONTRACTS ON CONTRACTS.FACULTY_ID = FACULTY.ID)
        WHERE CONTRACTS.END_DATE IS NULL
        ORDER BY DEPT.NAME ASC, FACULTY.HIRE_DATE ASC
    """) #this is all the data we need, all sorted. now just need to format it 
    print("Format: Department, Last Name, First Name, Title, Salary, Hire Date, Contract Start, Contract End")
    for facultyEntry in facultyData:
        print(f"{facultyEntry[0]},{facultyEntry[1]},{facultyEntry[2]},{facultyEntry[3]},{str(facultyEntry[4])},{str(facultyEntry[5])},{str(facultyEntry[6])},{str(facultyEntry[7])},")
        #little bit of a lengthy print statement but needed to cast the decimal and datetime objects to string outputs
    
def getActiveFacultyCount():
    facultyCount = exec_get_one("""
    SELECT COUNT(FACULTY.ID) FROM 
        ((((FACULTY INNER JOIN DEPT ON FACULTY.DEPT_ID = DEPT.ID)
        INNER JOIN POSITION ON FACULTY.POSITION_ID = POSITION.ID)
        INNER JOIN SALARIES ON SALARIES.FACULTY_ID = FACULTY.ID)
        INNER JOIN CONTRACTS ON CONTRACTS.FACULTY_ID = FACULTY.ID)
        WHERE CONTRACTS.END_DATE IS NULL
    """)
    print(f"Number of active faculty={facultyCount[0]}")

def getProfessors():
    allProfessors = exec_get_all("""
        SELECT FACULTY.LAST_NAME, FACULTY.FIRST_NAME, DEPT.NAME, POSITION.TITLE FROM
        ((FACULTY INNER JOIN DEPT ON FACULTY.DEPT_ID = DEPT.ID)
        INNER JOIN POSITION ON FACULTY.POSITION_ID = POSITION.ID)
        WHERE (STRPOS(POSITION.TITLE, \'Professor\') > 0)
        ORDER BY DEPT.NAME ASC, POSITION.TITLE ASC, FACULTY.LAST_NAME ASC
    """)
    print("Format: Last Name, First Name, Department, Position title")
    for professor in allProfessors:
        print(f"{professor[0]},{professor[1]},{professor[2]},{professor[3]},")

def getAllFacultyAltSort():
    allFaculty = exec_get_all("""
        SELECT FACULTY.LAST_NAME, FACULTY.FIRST_NAME, DEPT.NAME, POSITION.TITLE FROM
        ((FACULTY INNER JOIN DEPT ON FACULTY.DEPT_ID = DEPT.ID)
        INNER JOIN POSITION ON FACULTY.POSITION_ID = POSITION.ID)
        ORDER BY DEPT.NAME ASC, POSITION.ID ASC, FACULTY.LAST_NAME ASC
    """)
    print("Format: Last Name, First Name, Department, Position title")
    for professor in allFaculty:
        print(f"{professor[0]},{professor[1]},{professor[2]},{professor[3]},")

def getAvgSalary():
    deptList = exec_get_all("SELECT NAME FROM DEPT ORDER BY DEPT.NAME ASC") #all valid departments, in the alphabetical order requested
    for dept in deptList: #iterate and grab averages
        avg = exec_get_one(f""" SELECT AVG(SALARIES.SALARY) FROM
            FACULTY INNER JOIN SALARIES ON SALARIES.FACULTY_ID = FACULTY.ID
            WHERE FACULTY.DEPT_ID = \'{getDeptIdFromName(dept[0])}\'
        """)
        print(f"{dept[0]},{round(avg[0], 2)},")#round and print out