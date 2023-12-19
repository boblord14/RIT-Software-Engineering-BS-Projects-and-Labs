import unittest
from tests.test_utils import *

class TestDBSchema(unittest.TestCase):

    def test_rebuild_tables(self):
        """Rebuild the tables"""
        post_rest_call(self, 'http://localhost:5000/manage/init')
        post_rest_call(self, 'http://localhost:5000/manage/load')
        count = get_rest_call(self, 'http://localhost:5000')
        self.assertEqual(len(count), 15)

    def test_rebuild_tables_is_idempotent(self):
        """Drop and rebuild the tables twice"""
        post_rest_call(self, 'http://localhost:5000/manage/init')
        post_rest_call(self, 'http://localhost:5000/manage/init')
        post_rest_call(self, 'http://localhost:5000/manage/load')
        actual = get_rest_call(self, 'http://localhost:5000')
        self.assertEqual("yeah this works", actual)