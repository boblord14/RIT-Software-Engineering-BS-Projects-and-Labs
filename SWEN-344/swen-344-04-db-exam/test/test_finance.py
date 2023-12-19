import unittest
from src.finance import *
from src.swen344_db_utils import connect


class TestExam(unittest.TestCase):
    def setUp(self):
        """Setup: This will run automatically before EVERY test"""
        rebuildTables()
        print("Setup done")

    def test_tables(self):
        """Check existence of the tables"""
        print("Question-0: Testing Tables")
        result = exec_get_all('SELECT COUNT(*) FROM DEPT')
        self.assertEqual(3, result[0][0], "Incorreect number of rows in DEPT table")
        result = exec_get_all('SELECT COUNT(*) FROM FACULTY')
        self.assertEqual(26, result[0][0], "Incorrect number of rows in FACULTY table")
        print("Test_tables done")

    def test_question_1(self):
        """Print out the faculty data list"""
        print("Question-1: Getting all faculty")
        getAllFaculty()
        print("test_question_1 done")

    def test_question_2(self):
        """Add a new hire"""
        print("Question-2: Hiring a new faculty member")
        newHire("John", "Watson", "Biology", "Adjunct", datetime.date(1899, 4, 1), 2100)
        getAllFaculty()
        print("test_question_2 done")
    
    def test_question_3(self):
        """Modify an existing contract"""
        print("Question-3: Updating a contract end date")
        updateContractEnd("Sherlock", "Holmes", datetime.date(1893,12,15))
        displayAllContracts()
        print("test_question_3 done")
    
    def test_question_4(self):
        """Get all hires after 2021"""
        print("Question-4: Getting all hires after a specified year")
        getAllHiresAfterYear(2021)
        print("test_question_4 done")

    def test_question_5(self):
        print("Question-5: Get latest hire")
        getLatestHire()
        print("test_question_5 done")

    def test_question_6(self):
        print("Question-6: Get active faculty")
        getActiveFaculty()
        print("test_question_6 done")

    def test_question_7(self):
        print("Question-7: Get active faculty count")
        getActiveFacultyCount()
        print("test_question_7 done")

    def test_question_8(self):
        print("Question-8: Get all professors")
        getProfessors()
        print("test_question_8 done")

    def test_question_9(self):
        print("Question-9: Get all faculty with pos id")
        getProfessors()
        print("test_question_9 done")

    def test_question_10(self):
        print("Question-10: Get avg salary per dept")
        getAvgSalary()
        print("test_question_10 done")