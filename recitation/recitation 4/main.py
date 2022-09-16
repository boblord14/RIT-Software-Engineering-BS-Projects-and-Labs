"""
CSAP/X Recitation Exercise
04-Hashing
Hash Table Experiments

Students will conduct 4 experiments with the open addressing hash table
implementation from lecture.
"""

from hashtable import *


@dataclass
class Vehicle:
    """
    A Vehicle, e.g. Vehicle(make='Toyota', model=' Camry', year=2018).
    """
    make: str
    model: str
    year: int


def hash_vehicle(vehicle):
    """
    A hash function for Vehicles.
    :param vehicle: the vehicle
    :return: the Vehicle's hash code
    """
    return vehicle.year


def read_vehicles():
    """
    Creates a new hash table and populates it with Vehicles that are read
    in from the file 'vehicles.txt'.
    :return:
    """
    vehicles = create_hash_table(hash_vehicle, 30)
    count = 1
    with open('vehicles.txt') as f:
        for line in f:
            fields = line.split(',')
            make = fields[0]
            model = fields[1]
            year = int(fields[2])
            put(vehicles, Vehicle(make, model, year), count)
            count += 1
    return vehicles


def table_key_test(key, value):
    """
    A test function that tries to create a hash table with a given key and value.
    If it is successful it prints out the table.
    :param key: the key
    :param value: the value
    """
    table = create_hash_table(hash, 2)
    put(table, key, value)
    print(hash_table_to_str(table))


def main():
    """
    Conduct the four experiments with the CS hash table.
    """
    try:
        print('Experiment #1: hash table with a list as key')
        # TODO: Step1 - call table_key_test with a list as the key (any value)
        h=[6,6,3]
        table_key_test(h, 6)
    except TypeError as e:
        print(e)

    try:
        print('\nExperiment #2: hash table with a tuple as key')
        # TODO: Step 2 - call table_key_test with a tuple as the key (any value)
        h=(6,6,3)
        table_key_test(h, 6)
    except TypeError as e:
        print(e)

    try:
        print('\nExperiment #3: hash table with a Vehicle as key')
        # TODO: Step 3 - create a Vehicle (your choice of data) and call table_key_test with it as the key (any value)
        hh = Vehicle(make='hhh', model=' hhhh', year=202)
        # TODO: Step 4 - change the declaration of the Vehicle class so it is hashable.
    except TypeError as e:
        print(e)

    try:
        # TODO: Step 5 - Add collision counting to the hash table implementation
        print('\nExperiment #4: hash table with a frozen Vehicle as key and hash_vehicle() as hash function')
        vehicles = read_vehicles()  # tests put of new vehicles
        atlas = Vehicle('Volkswagen', 'Atlas', 2017)
        print('atlas value?', get(vehicles, atlas))  # tests get
        tesla = Vehicle('Tesla', 'Model S', 2019)
        print('tesla in vehicles?', has(vehicles, tesla))  # tests has
        print(hash_table_to_str(vehicles))
        # TODO: Step 6 - Modifying hash_vehicle() with a better hash function that reduces the total collisions
    except TypeError as e:
        print(e)


if __name__ == '__main__':
    main()