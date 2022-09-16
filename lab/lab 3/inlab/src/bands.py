"""
CSAPX Lab 3: Battle of the Bands
Given a list of bands and the number of votes they recived, find the most mediocre band (i.e. the band with the median amount of votes)

$ python3 bands.py [slow|fast] input-file

Author: RIT CS
Author: YOUR NAME HERE
"""

from dataclasses import dataclass
import sys  # argv
import time  # clock
import random  # random

from typing import List  # List


@dataclass
class Band:
    names: str
    votes: int


def populateBands(filename: str):
    Bands = []
    with open(filename, encoding="utf-8") as f:
        for line in f:
            temp = line.split('\t')
            Bands.append(Band(temp[0], int(temp[1])))
    return Bands


def _partition(data: list[Band], pivot: int) \
        -> tuple[list[Band], list[Band], list[Band]]:
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


def quick_sort(data: list[Band]) -> list[Band]:
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


def main() -> None:
    """
   The main function.
   :return: None
   """
    baseList = populateBands('test-100k.txt')
    sortedList = quick_sort(baseList)
    print("The most mediocre band is: " + str(sortedList[(len(baseList))//2]))

if __name__ == '__main__':
    main()
