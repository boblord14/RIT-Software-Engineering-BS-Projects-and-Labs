"""
A short demonstration program that just shows how to use dataclasses
to define simple, data-only "classes"

author: James Heliotis
"""

from dataclasses import dataclass

# The following line defines a type.
@dataclass
class RIT_building:
    name: str
    number: int


# these are global instances of the type RIT_building.
computing = RIT_building( "Golisano", 70 )
engineering = RIT_building( "James Gleason", 9 )
cias_A = RIT_building( "Booth", "7A" )
cias_B = RIT_building( "Gannett", "7B" )
union = RIT_building( "Student Alumni Union", 4 )

# this is a tuple of instance references.
buildings = ( computing, engineering, cias_A, cias_B, union )  # parens are not necessary here

# the for-each works as expected
for building in buildings:
    print( "Building #", building.number, "is now", building.name, "Hall." )

# dataclass instances are mutable, so be careful!
computing.number = 99
print("Changed Golisano to building", computing.number)

