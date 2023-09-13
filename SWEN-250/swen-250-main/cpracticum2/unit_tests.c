// C pointers Practicum2
// SWEN-250
// unit_tests
// Larry Kiser updated March 30, 2021

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>

#include "cpracticum2.h"
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
	
	
	printf( "\nTests for compare_arrays..................................\n" ) ;
	
	int values1[] = { 2, -5, 77, -1   } ;
	int values2[] = { 2, -5, 77, 33, -1   } ;
	int values3[] = { 3, -5, 77, 33, -1   } ;
	int values4[] = { 2, -5, 77, 31, -1   } ;
	// Test 1
	assert( compare_arrays( values1, NULL ) == 3,
		"Expect 3 since 2nd array is NULL" )
		? passcount++ : failcount++ ;
	// Test 2
	assert( compare_arrays( NULL, values2 ) == 3,
		"Expect 3 since 1st array is NULL" )
		? passcount++ : failcount++ ;
	// Test 3 
	assert( compare_arrays( &values2[1], &values3[1] ) == 0,
		"Expect 0 since these two arrays are the same if you start at index 1" )
		? passcount++ : failcount++ ;
	// Test 4
	assert( compare_arrays( values2, values3 ) == 2,
		"Expect 2 since first value is different with arrays the same length" )
		? passcount++ : failcount++ ;
	// Test 5
	assert( compare_arrays( values2, values4 ) == 2,
		"Expect 2 since last value is different with arrays the same length" )
		? passcount++ : failcount++ ;
	// Test 6
	assert( compare_arrays( values1, values2 ) == 1,
		"Expect 1 since 2nd array is longer" )
		? passcount++ : failcount++ ;
		
	printf( "\nTests for sum_each_number.................................\n" ) ;
	// Test 7
	assert( sum_each_number( 8 ) == 8,
		"Expect 8 this is the first time it is called" )
		? passcount++ : failcount++ ;
	// Test 8
	assert( sum_each_number( 3 ) == 11,
		"Expect 11 because 8 + 3 is 11." )
		? passcount++ : failcount++ ;
	// Test 9
	assert( sum_each_number( 7 ) == 18,
		"Expect 18 since 11 + 7 is 18." )
		? passcount++ : failcount++ ;
		
	printf( "\nTests for fix_bad_code...................................\n" ) ;
	char bad_code_string[] = "myteststring" ;
	char expected_bad_code_string[] = "myAeststring" ;
	// Test 10
	assert( fix_bad_code( NULL ) == 0,
		"Expect 0 since it is a NULL pointer" )
		? passcount++ : failcount++ ;
	// Test 11
	assert( fix_bad_code("") == 0,
		"Expect 0 since it is an empty string" )
		? passcount++ : failcount++ ;
	// Test 12
	assert( fix_bad_code(bad_code_string) == 1,
		"Expect 1 since it is not any empty string" )
		? passcount++ : failcount++ ;
	// Test 13
	assert( strcmp( bad_code_string, expected_bad_code_string ) == 0,
		"Expect A in third position of bad_code_string but got this string %s", bad_code_string )
		? passcount++ : failcount++ ;
		
	printf( "\nTests for create_key_value...............................\n" ) ;
	// Test 14
	struct key_value_pair *pair1 ;
	pair1 = create_key_value( "LengthyKey", "12345678901234567890" ) ;
	assert( pair1 != NULL,
		"Expect a valid pointer but got back a NULL struct key_value_pair pointer" )
		? passcount++ : failcount++ ;
	// Test 15
	assert( pair1 != NULL && pair1->key != NULL,
		"Expect a valid pointer but got back a NULL key pointer" )
		? passcount++ : failcount++ ;
	// Test 16
	assert( pair1 != NULL && pair1->value != NULL,
		"Expect a valid pointer but got back a NULL value pointer" )
		? passcount++ : failcount++ ;
	// Test 17
	assert( pair1 != NULL && pair1->key != NULL && strcmp( pair1->key, "LengthyKey" ) == 0,
		"Expect the key string to be LengthyKey but got %s", pair1 && pair1->key ? pair1->key : "NULL" )
		? passcount++ : failcount++ ;
	// Test 18
	assert( pair1 != NULL && pair1->value != NULL && strcmp( pair1->value, "12345678901234567890" ) == 0,
		"Expect value string to be 12345678901234567890 but got %s", pair1 && pair1->value ? pair1->value : "NULL" )
		? passcount++ : failcount++ ;

	// Test 19

	// NOTE -- if this is crashing it is because of an error in your implementation
	//         of create_key_value. If you cannot figure out what to do change
	//         your code to always return NULL.
	if ( pair1 && pair1->key && pair1->value )
	{
		free( pair1->key ) ;
		free( pair1->value ) ;
		free( pair1 ) ;
		passcount++ ;
	}
	else
		assert( 0,  "SKIPPING pair1 free due to NULL pointer(s)" ) ? passcount++ : failcount++ ;

	printf( "\nSummary of unit tests:\n%d tests passed\n%d tests failed\n\n", passcount, failcount ) ;
	
	return failcount ;
}
