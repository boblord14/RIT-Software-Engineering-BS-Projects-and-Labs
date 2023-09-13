// C pointers light Second Practicum
// SWEN-250
// Larry Kiser March 31, 2021
// Larry Kiser updated March 23, 2022

// struct for create_copy function
struct key_value_pair
{
	char *key ;
	char *value ;
} ;

// cpracticum2 functions
int compare_arrays( int *pfirst, int *psecond ) ;
int sum_each_number( int value ) ;
int fix_bad_code( char *pstring ) ;
struct key_value_pair *create_key_value ( char *key, char *value ) ;

