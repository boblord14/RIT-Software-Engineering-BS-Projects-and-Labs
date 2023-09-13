// unit_tests.c
// Larry Kiser Feb. 13, 2018

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>

#include "cpracticum1.h"
#include "unit_tests.h"


// Simple boolean assert function for unit testing
int assert( int test_result, char error_format[], ... ) {
	va_list arguments ;
	static int test_number = 1 ;
	int result = 1 ;	// return 1 for test passed or 0 if failed
	
	if ( ! test_result ) {
		va_start( arguments, error_format ) ;
		printf( "Test # %d failed: ", test_number ) ;
		vprintf( error_format, arguments ) ;
		printf( "\n" ) ;
		result = 0 ;
	}
	test_number++ ;
	return result ;
}

//////////////////////////////////////////////////////////////////////////
// Begin unit tests //////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

// do the unit tests
int test()
{
	int passcount = 0 ;
	int failcount = 0 ;
	int result ;
	char buffer[100] ;
	
	printf( "\n----------------- testing fix_bad_code ------------------------------------------\n" ) ;
	// Test 1 -- 
	buffer[0] = '\0' ;
	result = fix_bad_code( buffer, 'A' ) ;
	assert( result == 0,
		"Expected 0 because we passed an empty string but got %d", result )
		? passcount++ : failcount++ ;
		
	// Test  2 -- 
	strcpy( buffer, "abcdef" ) ;
	result = fix_bad_code( buffer, 'A' ) ;
	assert( result == 0,
		"Expected 0 because the passed string does not have 'A' but got  %d", result )
		? passcount++ : failcount++ ;
		
	// Test  3 -- 
	result = fix_bad_code( buffer, 'c' ) ;
	assert( result == 1,
		"Expected 1 because the passed string does have one 'c' but got  %d", result )
		? passcount++ : failcount++ ;
		
	// Test  4 -- 
	strcpy( buffer, "aaxxxa" ) ;
	result = fix_bad_code( buffer, 'a' ) ;
	assert( result == 3,
		"Expected 3 because the passed string has 3 'a' but got  %d", result )
		? passcount++ : failcount++ ;

	printf( "\n----------------- testing swap_first_and_last -----------------------------------\n" ) ;
	// Test 5 -- 
	result = swap_first_and_last( "" ) ;
	assert( result == 0,
		"Empty string must return 0. You returned %d", result )
		? passcount++ : failcount++ ;
		
	// Test 6 -- 
	strcpy( buffer, "x" ) ;
	result = swap_first_and_last( buffer ) ;
	assert( result == 0,
		"A single character string cannot be swapped. Expected 0 but got %d", result )
		? passcount++ : failcount++ ;
		
	// Test 7 -- 
	strcpy( buffer, "123" ) ;
	result = swap_first_and_last( buffer ) ;
	assert( result == 1,
		"String has 3 characters so can be swapped. Expected 1 but got %d", result )
		? passcount++ : failcount++ ;
		
	// Test 8 -- 
	result = strcmp( buffer, "321" ) ;	// The 3 and the 1 must be swapped
	assert( result == 0,
		"The passed string should now be 321. Your string is %s", buffer )
		? passcount++ : failcount++ ;
		
	// Test 9 -- 
	strcpy( buffer, "Much longer line!" ) ;
	result = swap_first_and_last( buffer ) ;
	assert( result == 1,
		"Expected 1 returned because it is a long line but got %d", result )
		? passcount++ : failcount++ ;
		
	// Test 10 --
	result = strcmp( buffer, "!uch longer lineM" ) ;
	assert( result == 0,
		"Expected !uch longer lineM but got %s", buffer )
		? passcount++ : failcount++ ;
	
	printf( "\n----------------- testing update_digits -----------------------------------------\n" ) ;
	// Test 11 --
	buffer[0] = '\0' ; 	// make buffer an empty string
	update_digits( buffer ) ;
	assert( buffer[0] == '\0',
		"An empty string must not be altered by update_digits. The end of string character was changed." )
		? passcount++ : failcount++ ;
		
	// Test 12 --
	strcpy( buffer, "0" ) ;
	update_digits( buffer ) ;
	assert( strcmp( buffer, "9" ) == 0,
		"Expected a string with just a digit '9' but your string is %s", buffer )
		? passcount++ : failcount++ ;
		
	// Test 13 --
	strcpy( buffer, "abc9de" ) ;
	update_digits( buffer ) ;
	assert( strcmp( buffer, "abc8de" ) == 0,
		"Expected abc7de but your string is %s", buffer )
		? passcount++ : failcount++ ;
		
	// Test 14 --
	strcpy( buffer, "1234567890" ) ;
	update_digits( buffer ) ;
	assert( strcmp( buffer, "0123456789" ) == 0,
		"Expected 0123456789 but your string is %s", buffer )
		? passcount++ : failcount++ ;
		
	// Test 15 --
	char test11[] = "@#%&^&$&*(" ;		// test for no digits
	update_digits( test11 ) ;
	assert( strcmp( test11, "@#%&^&$&*(" ) == 0,
		"Expected @#%&^&$&*( but your string is %s", test11 )
		? passcount++ : failcount++ ;
		

	printf( "\n----------------- testing get_string_statistics ---------------------------------\n" ) ;

	// Test 16 --
	struct statistics counts ;
	counts = get_string_statistics( "" ) ;	// test empty string case
	assert( counts.alpha_count == 0 && counts.numeric_count == 0,
		"Expected counts of 0 and 0 but got alpha %d and numeric %d", counts.alpha_count, counts.numeric_count )
		? passcount++ : failcount++ ;		

    // Test 17 --
    counts = get_string_statistics( "###4" ) ;  // test one digit case at end of string
    assert( counts.alpha_count == 0 && counts.numeric_count == 1,
        "Expected counts of 0 and 1 but got alpha %d and numeric %d", counts.alpha_count, counts.numeric_count )
        ? passcount++ : failcount++ ;

    // Test 18 --
    counts = get_string_statistics( "x######" ) ;  // test one alpha at beginning of the string
    assert( counts.alpha_count == 1 && counts.numeric_count == 0,
        "Expected counts of 1 and 0 but got alpha %d and numeric %d", counts.alpha_count, counts.numeric_count )
        ? passcount++ : failcount++ ;

    // Test 19 --
    counts = get_string_statistics( "ab$$cd123$$$$456" ) ;  // test complex string case
    assert( counts.alpha_count == 4 && counts.numeric_count == 6,
        "Expected counts of 4 and 6 but got alpha %d and numeric %d", counts.alpha_count, counts.numeric_count )
        ? passcount++ : failcount++ ;

		
	printf( "\nSummary of unit tests:\n%d tests passed\n%d tests failed\n\n", passcount, failcount ) ;
	
	return failcount ;
}
