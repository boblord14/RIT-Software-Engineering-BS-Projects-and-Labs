"""
file: arg_demo.py
Topic: Lists and Command Line Arguments

To run:
    $ python3 arg_demo.py arg1 arg2 ...
"""

import sys  # argv is the 'argument vectors' on the command line.


def main( ):
    """
    The main function echoes (prints) the command line arguments, if any.
    """
    print( "main Command line arguments:" )

    num_args = len( sys.argv )
    if num_args == 0:
        print( "none at all" ) # How does this happen?
    else:
        for i in range( num_args ):
            print( str( i ) + ": " + sys.argv[ i ] )
    print( )


def main_with_args( args ):
    """
    The main function echoes (prints) the command line arguments, if any.
    """
    print( "main_with_args arguments:" )

    num_args = len( args )
    if num_args == 0:
        print( "none at all" )
    else:
        for i in range( num_args ):
            print( str( i ) + ": " + str( args[ i ] ) )


if __name__ == '__main__':
    main( )            # function will read directly from sys.argv
    main_with_args( sys.argv )  # function will pick up arguments from sys.argv
else:
    print( """Sorry, when you import a module,
            You can't pass arguments to it.""" )

