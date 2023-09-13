// C (no pointers) Practicum
// SWEN-250
// Larry Kiser Feb. 13, 2018
//             New first no pointers practicum without structs
// Larry Kiser Sept. 27, 2022 updated
//             Feb. 22, 2023 added struct function


struct statistics
{
	int alpha_count ;	// number of alphabetic characters in string
	int numeric_count ;	// number of numeric characters (0 through 9)
} ;

// cpracticum1 functions
int swap_first_and_last( char mystring[] ) ;
void update_digits( char mystring[] ) ;
char range_test( int value, int lower_limit, int upper_limit ) ;
int fix_bad_code( char mystring[], char find_this ) ;
struct statistics get_string_statistics( char mystring[] ) ;
