// C (no pointers) Practicum
// SWEN-250
// Larry Kiser Feb. 13, 2018
//             New no pointers practicum without structs
// Revised Feb. 28, 2021 for 250-01 term 2205
// Revised Feb. 15, 2021 for 250-04 term 2215
// Revised Sept 27, 2022 for 250-01 term 2221
// Revised Feb. 22, 2022 for 250-02 term 2225
//         added one function on structs for 2225


#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

#include "cpracticum1.h"
#include "unit_tests.h"

// This function is implemented incorrectly. You need to correct it.
// It is supposed to count the number of times that the find_this character
// is present in the passed string. It returns this integer count.
// Example: "" searching for 'c' returns 0.      // (empty strings always return a 0)
// Example: "abcdef" searching for 'c' returns 1.
// Example: "abcdef" searching for 'g' returns 0.
// Example: "aaxxxa" searching for 'a' returns 3.
// NOTE -- you are required to correct this code. You are not allowed to completely rewrite it.
//         There are several errors in this routine. You must fix all errors. It is possible that the
//         unit tests may pass without all errors being corrected. Make sure that your corrected
//         code does not produce any warnings.
int fix_bad_code( char mystring[], char find_this )
{
	int total =  0 ;
	
	for ( int i = 0 ; mystring[i] != '\0' ; i++ ){
		if ( mystring[i] == find_this ){
			total++ ;
		}
	}
	return total ;
} 
//done. all unit tests pass.


// This function swaps the first character and the last character in
// the passed string. Only do this swap if the passed string has two
// or more characters.
// Example: "First" becomes "tirsF")
// The F was the first character and is now the last.
// The t was the last character and is now the first.
//
// Return 1 if the first and last were swapped.
// Return 0 if the passed string is an empty string.
// Return 0 if the passed string only has one character.
//
// You are allowed to use the strlen library function. 
int swap_first_and_last( char mystring[] )
{	int len = strlen(mystring);
	if(len>1){//strlen starts at 1 and ignores the null terminator 
		char temp = mystring[0];//standard swap w/ temp var
		mystring[0]=mystring[len-1];
		mystring[len-1] = temp;
		return 1;
	}else{
		return 0 ;
	}
}
//done. just test to confirm that the swap works right eventually.


// For the passed string you must change every character that is an ASCII digit in the passed string.
// If the digit is '1' through '9' you must subtract one ('1' becomes '0' and '9' become '8').
// If the digit is '0' you must change it to a '9'. 
// Do not change make any other changes to the array.
// For example a string "ab109xC0" would become "ab098xC9".
//
// The string may be an empty string. If it is empty do nothing.
// Note that this means you are directly changing the characters in the passed string.
// Hint -- you may want to use the isdigit function to make your code easier.
void update_digits( char mystring[] )
{
	for(int i=0; mystring[i] != '\0'; i++){
		int ascii = (int)mystring[i];
		if(isdigit(ascii)){//range check
			if(ascii == 48){//is 0 for special case
			mystring[i] = (char)57;
			}  else{//default case
			mystring[i] = (char)(mystring[i]-1);
			}
		}
	}
}
//done. unit tests check the end result so should be fine


// For your convenience the following is a copy of the struct statistics
// definition contained in cpracticum1.h. The actual definition is in the header file.
//
// struct statistics
// {
//    int alpha_count ;   // number of alphabetic characters in string
//    int numeric_count ; // number of numeric characters (0 through 9)
//} ;

// Count the number of alphabetic characters in mystring ('a' through 'z' and 'A' through 'Z').
// Count the number of numeric characters in mystring ('0' through '9').
// I included an instance of struct statistics. It is initialized to illegal values of -1 for
// the two counts. These bogus values cause all unit tests to fail. You are allowed to change
// these initialization values.
// Store these counts in the corresponding counts in struct statistics counts.
// Keep the return of this counts struct at the end of this function.
// If mystring is an empty string return the struct with counts of 0 for both struct counts.
// NOTE: you can use string functions like isalpha, isdigit, and strlen.
struct statistics get_string_statistics( char mystring[] )
{
	struct statistics counts = { 0, 0 } ;	
	
	for(int i=0; mystring[i]!= '\0'; i++){
		int ascii = (int)mystring[i];
		if(isdigit(ascii)){
		counts.numeric_count++;
		}
		if(isalpha(ascii)){
		counts.alpha_count++;
		}
	}
	return counts ;
}
//done. unit tests check the vals so works right. 


// Only runs the unit tests.
int main( int argc, char *argv[] )
{
	test() ;
}

