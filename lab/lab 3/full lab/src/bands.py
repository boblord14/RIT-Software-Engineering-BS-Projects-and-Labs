"""
CSAPX Lab 3: Battle of the Bands
Given a list of bands and the number of votes they recived, find the most mediocre band (i.e. the band with the median amount of votes)

$ python3 bands.py [slow|fast] input-file

Author: RIT CS
Author: Ethan Patterson
"""

from dataclasses import dataclass
import sys  # argv
import time  # clock
import random  # random

from typing import List  # List


@dataclass  # bands go here
class Band:
    names: str
    votes: int


def populateBands(filename: str):  # bands of bands go here
    Bands = []
    with open(filename, encoding="utf-8") as f:
        for line in f:
            temp = line.split('\t')
            Bands.append(Band(temp[0], int(temp[1])))
    return Bands


def _partition(data: list[Band], pivot: int) \
        -> tuple[list[Band], list[Band], list[Band]]:  # yoinked from inlab
    """
    Three way partition the data into smaller, equal and greater lists,
    in relationship to the pivot
    :param data: The data to be sorted (a list)
    :param pivot: The value to partition the data on
    :return: Three list: smaller, equal and greater
    """
    less, equal, greater = [], [], []
    for element in data:
        if element.votes < pivot:
            less.append(element)
        elif element.votes > pivot:
            greater.append(element)
        else:
            equal.append(element)
    return less, equal, greater


def quick_sort(data: list[Band]) -> list[Band]:  #yoinked from inlab
    """
    Performs a quick sort and returns a newly sorted list
    :param data: The data to be sorted (a list)
    :return: A sorted list
    """
    if len(data) == 0:
        return []
    else:
        pivot = data[0].votes
        less, equal, greater = _partition(data, pivot)
        return quick_sort(less) + equal + quick_sort(greater)


def quick_select(data: list[Band], k: int) -> list[Band]:  # takes list input and spits out the median
    if len(data) == 0:
        return []
    else:
        pivot = data[k].votes
        less, equal, greater = _partition(data, pivot)
        if len(less) <= k < (len(equal) + len(less)):  # found it
            return equal
        elif k < len(less):  # Data is in the earlier section (within `less` set)
            return quick_select(less, k)
        else:
            return quick_select(greater, ((k - len(less)) - len(equal)))  # Data is in the later section


def main() -> None:
    """
   The main function.
   :return: None
   """
    start = time.perf_counter()  # time stuff
    baseList = populateBands(str(sys.argv[2]))  # functional list from txt
    print("Search Type: " + str(sys.argv[1]))  # output 1
    print("Number of Bands: " + str(len(baseList)))  # output 2
    if str(sys.argv[1]) == "fast":  # quicksearch
        result = (quick_select(baseList, len(baseList) // 2))
        print("Elapsed Time: " + str(time.perf_counter() - start))
        print(result)
    else:  # quicksort
        sortedList = quick_sort(baseList)
        result = (str(sortedList[(len(baseList)) // 2]))
        print("Elapsed Time: " + str(time.perf_counter() - start))
        print("The most mediocre band is: " + result)


if __name__ == '__main__':
    main()
