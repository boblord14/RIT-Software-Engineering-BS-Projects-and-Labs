"""
CSCI-140/242 Computer Science APX Recitation Exercise
01-PythonIntro
Palindrome

Compute  whether an inputted word is a palindrome or not.  It computes this
both iteratively and recursively (not required by students - only if they
finish the iterative one quickly).

This is the student starter code.
"""

def is_palindrome(word):
    """
    A boolean function that iteratively tests whether a word is a palindrome
    or not.
    :param word: the word
    :return: whether it is a palindrome or not
    """

    test = 0
    while (test >= (len(word) - (test + 1)))==False:
        if word[test:(test+1)] != word[len(word)-(test+1)]:
            print( "firstletter:" + word[test:(test+1)])
            print("secondletter:" + word[len(word)-(test+1)])
            return False
        test+=1
    return True




def is_palindrome_rec(word):
    """
    A boolean function that recursively tests whether a word is a palindrome
    or not.
    :param word: the word
    :return: whether it is a palindrome or not
    """
    test=0

    if word[test:(test + 1)] != word[len(word) - (test + 1)]:
        return False

    if (test >= (len(word) - (test + 1))):
        return True
    else:

        return is_palindrome_rec(word[1:(len(word)-1)])




def main():
    """
    Prompt the user to enter a word and detect whether it is a palindrome
    or not.
    :return: None
    """
    word = input('Enter a word: ')
    print('Is ' + word + ' a palindrome?', is_palindrome(word))
    print('Is ' + word + ' a palindrome?', is_palindrome_rec(word))

if __name__ == '__main__':
    main()