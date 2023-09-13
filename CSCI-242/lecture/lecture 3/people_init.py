"""
CSAPX Week 3: Searching and Sorting

Author: Sean Strout @ RIT CS

A program that searches for a person by first and last name using:

- linear search: O(N)
- binary search: O(logN)

The person is a namedtuple that is read in from a csv file in the format:
    id, first, last, email, gender, ip

"""

from dataclasses import dataclass

"""
Person:
    id (int): Uniquely identified one person
    first (str): First name
    last (str): Last name
    email (str): Email address
    gender (str): Gender, "Male" or "Female"
    ip (str): IP address
"""
@dataclass
class Person:
    id: int
    first: str
    last: str
    email: str
    gender: str
    ip: str

def linear_search( people: list[ Person ], first: str, last: str ) -> int:
    """
    Perform a linear search for a person by first and last name in a list of
    Person and return the index of it, if present, or -1 if not present.
    :param people: A list of Person (potentially unordered)
    :param first: First name
    :param last: Last name
    :return: The index of the Person, or -1 if not present
    """
    for i in range( len( people ) ):
        if people[ i ].first == first and people[ i ].last == last:
            return i
    return -1


def binary_search_rec( people: list[ Person ], first: str, last: str,
                       start: int, end: int ) -> int:
    """
    The private recursive helper function for performing a binary search
    for a Person.
    :param people: A list of Person (potentially unordered)
    :param first: First name
    :param last: Last name
    :param start: The starting index in the list to search at
    :param end: The ending index in the list to search at
    :return: The index of the Person, or -1 if not present
    """
    return -1


def binary_search( people: list[ Person ], first: str, last: str ) -> int:
    """
    Perform a binary search for a person by first and last name in a list of
    Person and return the index of it, if present, or -1 if not present.
    :param people: A list of Person (potentially unordered)
    :param first: First name
    :param last: Last name
    :return: The index of the Person, or -1 if not present
    """
    return -1


def read_people( filename: str ) -> list[ Person ]:
    """
    Read people from a file into a list of Person namedtuples.
    :param filename: The name of the file
    :return: A list of Person
    """
    return [ ]


def test_searches( ):
    """
    Test function for searches
    :return:
    """
    print( 'Testing linear search...' )
    people = read_people( "data/1000.csv" )
    index = linear_search( people, 'Anne', 'Adams' )
    print( 'Anne Adams in 1000.csv (exists):',
           'OK' if index == 0 else "FAIL! Got: " + str( index ) )
    index = linear_search( people, 'Patrick', 'Lawson' )
    print( 'Patrick Lawson in 1000.csv (exists):',
           'OK' if index == 500 else "FAIL! Got: " + str( index ) )
    index = linear_search( people, 'Kenneth', 'Young' )
    print( 'Kenneth Young in 1000.csv (exists):',
           'OK' if index == 998 else "FAIL! Got: " + str( index ) )
    index = linear_search( people, 'Kenneth', 'YYY' )
    print( 'Kenneth YYY in 1000.csv (does not exist):',
           'OK' if index == -1 else "FAIL! Got: " + str( index ) )

    print( '\nTesting binary search...' )
    index = binary_search( people, 'Anne', 'Adams' )
    print( 'Anne Adams in 1000.csv (exists):',
           'OK' if index == 0 else "FAIL! Got: " + str( index ) )
    index = binary_search( people, 'Patrick', 'Lawson' )
    print( 'Patrick Lawson in 1000.csv (exists):',
           'OK' if index == 500 else "FAIL! Got: " + str( index ) )
    index = binary_search( people, 'Kenneth', 'Young' )
    print( 'Kenneth Young in 1000.csv (exists):',
           'OK' if index == 998 else "FAIL! Got: " + str( index ) )
    index = binary_search( people, 'Kenneth', 'YYY' )
    print( 'Kenneth YYY in 1000.csv (does not exist):',
           'OK' if index == -1 else "FAIL! Got: " + str( index ) )


if __name__ == '__main__':
    test_searches( )
