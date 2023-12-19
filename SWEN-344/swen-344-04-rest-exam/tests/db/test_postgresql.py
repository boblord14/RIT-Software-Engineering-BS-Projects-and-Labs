import unittest
from tests.test_utils import *

class TestPostgreSQL(unittest.TestCase):
    """Make sure the DB is loaded"""
    def test_can_connect(self):
        actual = get_rest_call(self, 'http://localhost:5000/manufacturer')
        assert(len(actual) > 0)
