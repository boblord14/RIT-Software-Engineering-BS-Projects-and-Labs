// unit_tests student additions
// Larry L. Kiser Oct. 22, 2015

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>

#include "analysis.h"
#include "unit_tests_analysis.h"
#include "unit_tests.h"

//////////////////////////////////////////////////////////////////////////
// Begin unit tests //////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

// do the unit tests
// returns the number of failed tests.
int test_student()
{
	int passcount = 0 ;
	int failcount = 0 ;
	struct linked_list list1 ;		// list list pointers for one list
	char *expected_word;

	// Initialize local stack data
	memset( &list1, 0, sizeof( list1 ) ) ;		// set all pointers to NULL (zero)
	
	printf( "\n========= student generated tests on MLK excerpt ===============================\n" ) ;
	int result = read_file( &list1, "MLK_dream_excerpt.txt" ) ;
	assert( result == 113,
		"1 read_file processing MLK_dream_excerpt. Expect word count of 113 returned but got %d.", result )
		? passcount++ : failcount++ ;


	// Add a test to verify that the word "day" is used three times in the list.
    assert(((find_word(&list1, "day") == 1) && (list1.p_current->one_word.word_count == 3)),
            "2 Specific word count of word 'day' in list. Expect count of 3 for the word 'day' returned but got %d.", list1.p_current->one_word.word_count )
    ? passcount++ : failcount++ ;
    
	// Add a test to verify that the word AFTER the word "have" is the word "heat".
    assert(((find_word(&list1, "have") == 1) && (strcmp(list1.p_current->p_next->one_word.unique_word, "heat") == 0)),
           "3 Check alphabetized placements in list. Expect word 'have' to be after word 'heat' but got %s.", list1.p_current->p_next->one_word.unique_word)
    ? passcount++ : failcount++ ;
    
	// Add a test to verify that the last word in the list is "with"
    expected_word = "with\0";
    assert(strcmp(list1.p_tail->one_word.unique_word, expected_word)==0,
           "4 Check last word in list. Expect word 'with' but got %s.", list1.p_tail->one_word.unique_word)
    ? passcount++ : failcount++ ;

	// Add a test to verify that the next to the last word is "will"
    expected_word = "will\0";
    assert(strcmp(list1.p_tail->p_previous->one_word.unique_word, expected_word)==0,
           "5 Check word before last word in list. Expect word 'will' but got %s.", list1.p_tail->p_previous->one_word.unique_word)
    ? passcount++ : failcount++ ; 	

	result = clear_linked_list( &list1 ) ;
	assert( result == 62,
		"6 clear linked list expected to remove 62 entries but got %d.", result )
		? passcount++ : failcount++ ;

	printf( "\nSummary of analysis unit tests:\n%d tests passed\n%d tests failed\n\n", passcount, failcount ) ;
	
	return failcount ;
}
