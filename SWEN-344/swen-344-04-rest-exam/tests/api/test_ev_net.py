import unittest
import json
from tests.test_utils import *
from src.db.swen344_db_utils import *

class TestEV_Net(unittest.TestCase):

    """Runs once on test start. resets the db to the pre unit test state. unlike setUp() this runs ONCE. 
    I don't know if im just missing something or if theres no easier way to just reset the db pre testing. This works though. """
    @classmethod
    def setUpClass(cls):
        exec_sql_file('src/db/schema.sql')

    #Small utility for printing a list of items; one per line
    def print_list(self, list):
        for item in list:
            print(item)

    def test_example_endpoint(self):
        actual = get_rest_call(self, 'http://localhost:5000/manufacturer')
        assert(len(actual) == 8)

    def test_search_endpoint(self):
        result = get_rest_call(self, 'http://localhost:5000/manufacturer/2')
        print(result)
        result = get_rest_call(self, 'http://localhost:5000/manufacturer/name/Ford')
        print(result)

    def test_01_inventory(self):
        result = get_rest_call(self, 'http://localhost:5000/inventory')
        print('\n Full inventory list:')
        TestEV_Net.print_list(self, result)

    def test_02_add_new_vehicle(self):
        print('\n Before:')
        TestEV_Net.print_list(self, get_rest_call(self, 'http://localhost:5000/inventory')) #print full vehicle inventory before adding
        jdata = json.dumps(dict(Manufacturer="BMW", Type="PLUG-IN HYBRID", Model="x45e", Description="a bit big, but comfy!", Quantity=3))
        hdr = {'content-type': 'application/json'}
        post_rest_call(self, 'http://localhost:5000/inventory', jdata, hdr)#add
        print('\n After:')
        TestEV_Net.print_list(self, get_rest_call(self, 'http://localhost:5000/inventory')) #print full vehicle inventory after adding

    def test_03_get_by_model(self):
        print('\n *** Prius Inventory ***')
        TestEV_Net.print_list(self, get_rest_call(self, 'http://localhost:5000/inventory?model=Prius'))

    def test_04_get_all_hybrids(self):
        print('\n *** ALL HYBRIDs ***')
        TestEV_Net.print_list(self, get_rest_call(self, 'http://localhost:5000/inventory?type=HYBRID')) 

    def test_05_sell_some_cars(self):
        print("\nOriginal Inventory:")
        TestEV_Net.print_list(self, get_rest_call(self, 'http://localhost:5000/inventory'))
        priusData = get_rest_call(self, 'http://localhost:5000/inventory?model=Prius')
        print(f"Prisues in inventory: {priusData[0][4]}")
        if(priusData[0][4]>=2):
            print("There are 2 or more priuses in inventory, selling 2...")
            jdata = json.dumps(dict(Quantity = (priusData[0][4]-2), Model=priusData[0][1]))
            hdr = {'content-type': 'application/json'}
            put_rest_call(self, 'http://localhost:5000/inventory', jdata, hdr)
            print(f"Inventory Afterwards:")
            TestEV_Net.print_list(self, get_rest_call(self, 'http://localhost:5000/inventory'))
        else:
            print("There is not 2 or more priuses in inventory, cant sell 2")

        

