"""
CSAP/X Pair Exercise
03-SearchSortListFiles
Stable Sorting

This is the starter code for the pair exercise.

A program that demonstrates the difference between how mergesort and quicksort
differ with respect to stable sorting.   In stable sorting, if two elements are
deemed to be equal, when sorted they should appear in the same order they
appear in the original list.

One of the sorts is always stable, which the other is not.  Which is which?

This program uses the input a text file, input.txt, which contains strings.
Each line is read as a list of strings and should be sorted by the *lengths*
of each strings (not alphabetically).

$ python3 stable_sorting.py
Before sorting: ['d', 'a', 'b', 'c']
After mergesort: ['a', 'b', 'c', 'd']
After quicksort: ['a', 'b', 'c', 'd']

Before sorting: ['banana', 'apple', 'blueberry', 'fruit', 'orange', 'pear']
After mergesort: ['apple', 'banana', 'blueberry', 'fruit', 'orange', 'pear']
After quicksort: ['apple', 'banana', 'blueberry', 'fruit', 'orange', 'pear']

Before sorting: ['c', 'bb', 'aa', 'b', 'a']
After mergesort: ['a', 'aa', 'b', 'bb', 'c']
After quicksort: ['a', 'aa', 'b', 'bb', 'c']

Before sorting: ['c', 'bb', 'aa', 'ccc', 'b', 'bbb', 'aaa', 'c', 'bb']
After mergesort: ['aa', 'aaa', 'b', 'bb', 'bb', 'bbb', 'c', 'c', 'ccc']
After quicksort: ['aa', 'aaa', 'b', 'bb', 'bb', 'bbb', 'c', 'c', 'ccc']
"""

def split(data):
    """
    Split the data using the even-odd split
    :param data: the list to split in half
    :return: two lists, split by even-odd indices
    """
    return data[::2], data[1::2]

def merge(left, right):
    """
    Merges two sorted list, left and right, into a combined sorted result
    :param left: A sorted list
    :param right: A sorted list
    :return: one combined sorted list
    """
    # the sorted left + right will be stored in result
    result = []
    left_index, right_index = 0, 0

    # loop through until either the left or right list is exhausted
    while left_index < len(left) and right_index < len(right):
        if left[left_index] <= right[right_index]:
            result.append(left[left_index])
            left_index += 1
        else:
            result.append(right[right_index])
            right_index += 1

    # take the un-exhausted list and extend the remainder onto the result
    if left_index < len(left):
        result.extend(left[left_index:])
    elif right_index < len(right):
        result.extend(right[right_index:])

    return result

def merge_sort(data):
    """
    Performs a merge sort and returns a newly sorted list
    :param data: The data to be sorted (a list)
    :return: A sorted list
    """
    # an empty list, or list of 1 element is already sorted
    if len(data) < 2:
        return data
    else:
        # split the data by even and odd indices
        left, right = split(data)
        # return the merged recursive merge_sort of the halves
        return merge(merge_sort(left), merge_sort(right))

def partition(data, pivot):
    """
    Three way partition the data into smaller, equal and greater lists,
    in relationship to the pivot
    :param data: the data to be sorted (a list)
    :param pivot: the value to partition the data on
    :return: three lists - smaller, equal and greater
    """
    less, equal, greater = [], [], []
    for element in data:
        if element < pivot:
            less.append(element)
        elif element > pivot:
            greater.append(element)
        else:
            equal.append(element)
    return less, equal, greater

def quick_sort(data):
    """
    Performs a quick sort and returns a newly sorted list
    :param data: The data to be sorted (a list)
    :return: A sorted list
    """
    if len(data) == 0:
        return []
    else:
        pivot = data[0]
        less, equal, greater = partition(data, pivot)
        return quick_sort(less) + equal + quick_sort(greater)

def main():
    """
    The main program opens input.txt, reads each line of data into a list of strings
    and sorts the list using mergesort and quicksort.
    """

    with open('input.txt') as f:                # open the file
        for line in f:                          # loop over each line in the file
            values = line.split()               # split the line into a list of strings
            print('Before sorting:', values)
            merge_values = merge_sort(values)
            print('After mergesort:', merge_values)
            quick_values = quick_sort(values)
            print('After quicksort:', quick_values)
            print()

if __name__ == '__main__':
    main()