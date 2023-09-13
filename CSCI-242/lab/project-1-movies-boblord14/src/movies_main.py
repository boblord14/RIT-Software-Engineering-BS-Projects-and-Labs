import sys
import time
from dataclasses import dataclass

'''
I use a combined dataclass for both the movies and the ratings. I know it was recommended to split them up but it 
works better for me personally to think through this way, and has no loss in functionality. 
'''


@dataclass  # movies go here
class Movie:
    titleType: str  # temp[1]
    primaryTitle: str  # temp[2]
    isAdult: str  # temp[4]
    startYear: int  # temp[5]
    runtimeMinutes: int  # temp[7]
    genres: str  # temp[8]
    averageRating: float  # temp[1] in second for loop
    votes: int  # temp[2] in second for loop


'''
incredibly janked together function to read and populate a dictionary from the text files

populateData reads both the basics and ratings files line by line and populates the dataSet dictionary with the tconst
as the id and the everything else as the data

first splits up the data into a temp array
then checks specific values in it and changes them if they dont exist or are \\N (which means nothing there) 
then populates the dictionary with the set of values
repeat times every entry in basics

after that, goes to the ratings file and uses the tconst as the key to go through the existing dictionary and tack on
the two values for the ratings at the end

once thats all finished, goes through the dictionary and removes the entries with the isAdult value set to 1

after that it does some math things to calculate the movie and rating numbers, and prints it
plus throughout the whole function some other generic things are printed out in console

the genre value needs some additional manipulation. first, if one doesnt exist in the data, append the temp list with 
a value of None, second if it exists and doesnt equal None, strip it, and third add proper spacing 
'''


def populateData(whichSet: str):  # input of data
    dataSet = {}
    with open(("data/" + whichSet + ".basics.tsv"), encoding="utf-8") as f:
        print("reading data/" + whichSet + ".basics.tsv into dict...")
        basicsTime = time.perf_counter()
        f.readline()
        for line in f:
            temp = line.split('\t')
            if temp[5] == "\\N":
                temp[5] = "0"
            if temp[7] == "\\N":
                temp[7] = "0"
            if 9 > len(temp):
                temp.append(None)
            if temp[8] is not None:
                temp[8] = temp[8].strip()
                temp[8] = temp[8].replace(",", ", ")
            if temp[8] == "\\N":
                temp[8] = "None"
            dataSet[temp[0]] = Movie(temp[1], temp[2], temp[4], int(temp[5]), int(temp[7]), temp[8], None, None)
        print("elapsed time (s): " + str(time.perf_counter() - basicsTime) + "\n")
    with open(("data/" + whichSet + ".ratings.tsv"), encoding="utf-8") as f:
        print("reading data/" + whichSet + ".ratings.tsv into dict...")
        ratingsTime = time.perf_counter()
        f.readline()
        for line in f:
            temp = line.split('\t')
            dataSet[temp[0]].averageRating = float(temp[1])
            dataSet[temp[0]].votes = int(temp[2])
        print("elapsed time (s): " + str(time.perf_counter() - ratingsTime) + "\n")

    for movieData in dataSet.copy():
        if int(dataSet[movieData].isAdult) == 1:
            del dataSet[movieData]

    num = len(dataSet)
    print("Total movies: " + str(num))
    noneCount = 0
    for movieData in dataSet:
        if dataSet[movieData].averageRating is None:
            noneCount += 1
    print("Total ratings: " + str(num - noneCount) + " \n")

    return dataSet


'''
simple lookup function, takes tconst input and dict input(just to be more abstract)
has some cases for missing data in the dictionary, returns a long string with the data where it needs to be and the 
elapsed time
'''


def lookup(tconst: str, dictData: dict) -> str:
    pTime = time.perf_counter()
    print("processing: LOOKUP " + tconst)
    temp = dictData.get(tconst, None)
    if temp is None:
        print("	Movie not found!")
        print("	Rating not found!")
    else:
        print("	MOVIE: Identifier: " + tconst + ", Title: " + temp.primaryTitle + ", Type: " + temp.titleType +
              ", Year: " + str(temp.startYear) + ", Runtime: " + str(temp.runtimeMinutes) + ", Genres: " + str(temp.
                                                                                                               genres))
        if temp.votes is None:
            print("	Rating not found!")
        else:
            print("	RATING: Identifier: " + tconst + ", Rating: " + str(temp.averageRating) + ", Votes: " + str(
                temp.votes))
    print("elapsed time (s): " + str(time.perf_counter() - pTime) + "\n")


'''
function to check if contains a thing of a certain type contains a specific string in the title

first takes the overall dictionary, and puts all the keys in a list(dictList), then as a for loop goes through it and 
checks,a variable increments every time. if a match is found, the variable denotes the position the key is in inside of 
dictList. this provides an easy method of pulling the keys for the right ones, and similar methods will be used for 
other functions

beyond that, this uses a fairly standard return statement/error catcher if none are found. 
'''


def contains(reqType: str, words: str, dictData: dict):
    pTime = time.perf_counter()
    print("processing: CONTAINS " + reqType + " " + words)
    dictList = list(dictData)
    tconstList = []
    index = 0
    for movieData in dictData:
        if (dictData[movieData].titleType == reqType) and (words in dictData[movieData].primaryTitle):
            tconstList.append(dictList[index])
        index += 1
    if len(tconstList) == 0:
        print("	No match found!")
    else:
        for tconst in tconstList:
            temp = dictData.get(tconst)
            print("	Identifier: " + tconst + ", Title: " + temp.primaryTitle + ", Type: " + temp.titleType +
                  ", Year: " + str(temp.startYear) + ", Runtime: " + str(temp.runtimeMinutes) + ", Genres: " +
                  temp.genres)
    print("elapsed time (s): " + str(time.perf_counter() - pTime) + "\n")


'''
year_and_genre returns entries in the dictionary of a specific type, year, and genre
the genre of the entry can have other genres too, just one of them must be the specified one

the code for this is basically the same as the code for the contains function, just with the third param/argument
some switched around arguments for what specifically is being looked for, and some sorting to get it alphabetical

the sorting uses sorted(list, key, object in list) to sort by alphabetical by setting object in list to be the dataclass
value. this should be replicable for any other functions that need sorting
'''


def year_and_genre(reqType: str, reqYear: int, reqGenre: str, dictData: dict):
    pTime = time.perf_counter()
    print("processing: YEAR_AND_GENRE " + reqType + " " + str(reqYear) + " " + reqGenre)
    dictList = list(dictData)
    tconstList = []
    index = 0
    for movieData in dictData:
        if (dictData[movieData].titleType == reqType) and (dictData[movieData].startYear == reqYear) and \
                (reqGenre in dictData[movieData].genres):
            tconstList.append(dictList[index])
        index += 1
    if len(tconstList) == 0:
        print("	No match found!")
    else:
        sortSet = {}
        for tconst in tconstList:
            sortSet[tconst] = dictData[tconst]
        for tconst, movie in sorted(sortSet.items(), key=lambda x: x[1].primaryTitle):
            print(
                "	Identifier: " + tconst + ", Title: " + movie.primaryTitle + ", Type: " + movie.titleType +
                ", Year: " + str(movie.startYear) + ", Runtime: " + str(
                    movie.runtimeMinutes) + ", Genres: " + movie.genres)
    print("elapsed time (s): " + str(time.perf_counter() - pTime) + "\n")


'''
very similar to the year_and_data function, but with one key difference:
runtime uses a double dose of the sorted() function to sort a series of tuples filled with (runtime, title, tconst)
as sorted() processes tuples by sorting based on the first and then for equal values sorts based on the second value

from there its almost sorted, just backwards, so another sorted() is ran but with 'reverse=True' as a parameter,
and only the first value(runtime) in the tuple is used to sort, so it goes in the proper highest->lowest runtime order
w/ the alphabetical sorting. 
'''


def runtime(reqType: str, minReq: int, maxReq: int, dictData: dict):
    pTime = time.perf_counter()
    print("processing: RUNTIME " + reqType + " " + str(minReq) + " " + str(maxReq))
    dictList = list(dictData)
    tconstList = []
    index = 0
    for movieData in dictData:
        if (dictData[movieData].titleType == reqType) and (dictData[movieData].runtimeMinutes >= minReq) and \
                (dictData[movieData].runtimeMinutes <= maxReq):
            tconstList.append(dictList[index])
        index += 1
    if len(tconstList) == 0:
        print("	No match found!")
    else:
        sortSet = []
        for tconst in tconstList:
            sortSet.append((dictData[tconst].runtimeMinutes, dictData[tconst].primaryTitle, tconst))
        runtimeSort = sorted(sortSet)
        reverseSort = sorted(runtimeSort, key=lambda x: x[0], reverse=True)
        for tconst in reverseSort:
            print(
                "	Identifier: " + tconst[2] + ", Title: " + dictData[tconst[2]].primaryTitle + ", Type: " +
                dictData[tconst[2]].titleType + ", Year: " + str(dictData[tconst[2]].startYear) + ", Runtime: " + str(
                    dictData[tconst[2]].runtimeMinutes) + ", Genres: " + dictData[tconst[2]].genres)
    print("elapsed time (s): " + str(time.perf_counter() - pTime) + "\n")


'''
pretty close to runtime. does the tuple sort trick to get easily sorted both numerically and alphabetically, 
and then a slightly different trick with grabbing the tconsts thats functionally identical but simplified

i could go back and adjust runtime too but if it aint broke dont fix it
'''


def most_votes(reqType: str, reqNum: int, dictData: dict):
    pTime = time.perf_counter()
    print("processing: MOST_VOTES " + reqType + " " + str(reqNum))
    typeList = []
    for dataPoint in dictData:
        if (dictData[dataPoint].titleType == reqType) and (dictData[dataPoint].votes is not None):
            typeList.append((dictData[dataPoint].votes, dictData[dataPoint].primaryTitle, dataPoint))
    if len(typeList) == 0:
        print("	No match found!")
    else:
        voteSort = sorted(typeList)
        reverseVoteSort = sorted(voteSort, key=lambda x: x[0], reverse=True)
        keyList = []
        for req in range(reqNum):
            if req < len(reverseVoteSort):
                keyList.append(reverseVoteSort[req][2])
                print("	" + str(req + 1) + ". VOTES: " + str(dictData[keyList[req]].votes) +
                      ", MOVIE: Identifier: " + keyList[req] + ", Title: " + dictData[
                          keyList[req]].primaryTitle + ", Type: " +
                      dictData[keyList[req]].titleType + ", Year: " + str(
                    dictData[keyList[req]].startYear) + ", Runtime: " + str(
                    dictData[keyList[req]].runtimeMinutes) + ", Genres: " + dictData[keyList[req]].genres)
    print("elapsed time (s): " + str(time.perf_counter() - pTime) + "\n")


'''
a much more complicated method than the others, similar to the most_votes method though. more selective params to parse
the main dict by, which then puts them into tuple form with the sortable parts and tconsts. 

this gets sorted, then a for loop handles each year on its own, grabs the top reqNum from that year, prints them, and
does the next year
'''


def top(reqType: str, reqNum: int, minYear: int, maxYear: int, dictData: dict):
    pTime = time.perf_counter()
    print("processing: TOP " + reqType + " " + str(reqNum) + " " + str(minYear) + " " + str(maxYear))
    typeList = []
    for dataPoint in dictData:
        if (dictData[dataPoint].titleType == reqType) and (dictData[dataPoint].averageRating is not None) and \
                (dictData[dataPoint].startYear >= minYear) and (dictData[dataPoint].startYear <= maxYear) and \
                (dictData[dataPoint].votes >= 1000):
            typeList.append(
                (dictData[dataPoint].startYear, dictData[dataPoint].averageRating, dictData[dataPoint].votes,
                 dictData[dataPoint].primaryTitle, dataPoint))
    topSort = sorted(typeList)
    for yearOffset in range((maxYear - minYear) + 1):
        annualList = []
        for validData in topSort:
            if validData[0] == (minYear + yearOffset):
                annualList.append(validData)
        print("	YEAR: " + str(minYear + yearOffset))
        if len(annualList) == 0:
            print("		No match found!")
        else:
            for req in range(reqNum):
                if req < len(annualList):
                    tconstLoc = ((req + 1) * -1)
                    tconst = annualList[tconstLoc][4]
                    print("		" + str(req + 1) + ". RATING: " + str(dictData[tconst].averageRating) + ", VOTES: " +
                          str(dictData[tconst].votes) + ", MOVIE: Identifier: " + tconst + ", Title: " +
                          dictData[tconst].primaryTitle + ", Type: " + dictData[tconst].titleType + ", Year: "
                          + str(dictData[tconst].startYear) + ", Runtime: " + str(dictData[tconst].runtimeMinutes)
                          + ", Genres: " + dictData[tconst].genres)
    print("elapsed time (s): " + str(time.perf_counter() - pTime) + "\n")


'''
main function, nothing too special here.
checks for sys.argv arguments, and throws the proper file name at populateData based around that.
after that is the input file read function, and a bunch of if/elif statements to manage said input. 

the elseifs are almost all identical except for contains, as that needs some extra working due to the way I handle input
arguments. as it splits by spaces, once it knows that the first argument dictates that it's a contains call, the part
of the input argument past the initial 2 arguments("CONTAINS" and the reqType), the rest is reassembled as a string and
that string is passed to the contains function

there's also an invalid input catcher tacked on at the end, which is wholly unnecessary as inputs are assumed to be 
correct, I just feel better having it there. 
'''


def main():
    if len(sys.argv) > 1:
        movieData = populateData("small")
    else:
        movieData = populateData("title")
    for line in sys.stdin:
        line = line.strip()
        argList = line.split(" ")
        if argList[0] == "LOOKUP":
            lookup(argList[1], movieData)
        elif argList[0] == "CONTAINS":
            queryArr = []
            for q in range(len(argList) - 2):
                queryArr.append(argList[q + 2])
            queryStr = ' '.join(queryArr)
            contains(argList[1], queryStr, movieData)
        elif argList[0] == "YEAR_AND_GENRE":
            year_and_genre(argList[1], int(argList[2]), argList[3], movieData)
        elif argList[0] == "RUNTIME":
            runtime(argList[1], int(argList[2]), int(argList[3]), movieData)
        elif argList[0] == "MOST_VOTES":
            most_votes(argList[1], int(argList[2]), movieData)
        elif argList[0] == "TOP":
            top(argList[1], int(argList[2]), int(argList[3]), int(argList[4]), movieData)
        else:  # I know these are assumed to be correct, but I feel better just having this catcher here
            print("Invalid input, please try again")


if __name__ == '__main__':
    main()
