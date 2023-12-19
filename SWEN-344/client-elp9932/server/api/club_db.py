from .swen_344_db_utils import *


"""Returns all clubs from the club table that has a city with a substring that matches the citySearch argument"""
def getClubs(citySearch):
    stringFormat = ' '.join(citySearch.split()) #cleans up whitespace garbage just in case
    stringFormat = '%'+stringFormat+'%'
    return exec_get_all("SELECT * FROM clubs WHERE city ILIKE %s ORDER BY id ASC", (stringFormat,))

"""Deletes a club given the club's id"""
def deleteClub(id):
    exec_commit("DELETE FROM clubs WHERE id = %s", (id,))

"""Takes in a full club object as a dict with all the fields + id and updates the (assumed preexisting) club with the new data set"""
def updateClub(clubData):
    exec_commit("UPDATE clubs SET name=%s, count=%s, yellow=%s, max=%s, city=%s, music=%s WHERE id=%s", (clubData['name'], clubData['count'], clubData['yellow'], clubData['max'], clubData['city'], clubData['music'], clubData['id'],))

"""Creates a new club given a dict of all the info it needs"""
def createClub(clubData):
    exec_commit("INSERT INTO clubs VALUES (DEFAULT, %s, DEFAULT, %s, %s, %s, %s);", (clubData['name'], clubData['yellow'], clubData['max'], clubData['city'], clubData['music'],))