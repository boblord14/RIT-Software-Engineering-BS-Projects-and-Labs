/*
 * Implementation of the read_data module.
 *
 * See read_data.h for a description of the read_data function's
 * specification.
 *
 * Note that the parameter declarations in this module must be
 * compatible with those in the read_data.h header file.
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "read_data.h"

// Reads the three fields from lines such as W$1349$1.414$ into
// local variables using getchar().
// Converts the two numeric fields to numbers using atoi for the
// integer fields and atof for the double.
// Using a correctly defined set of parameters (use pointers) pass
// those values back to the program that called this function.

void read_data( char *c, int *i, double *d) {

    *c = getchar();//easy one, single char

    getchar();//standalone getchar to skip the $ between the char and the int

    char temp_char = getchar();//stuff to parse the stdin via getchar until the $ is reached then convert that into an int
    char temp_string[25+1] = {0};
    for(int j = 0; j<26;j++){
        if(temp_char == '$'){
            break;
        }
        else{
            temp_string[j]=temp_char;
            temp_char=getchar();
        }
            }
    *i = atoi(temp_string);

    temp_char=getchar();//same as above but with the double
    temp_string[0] = '\0';
    for(int j = 0; j<26;j++){
        if(temp_char == '$'){
            break;
        }
        else{
            temp_string[j]=temp_char;
            temp_char=getchar();
        }
    }
    *d = atof(temp_string);

	return;
}

