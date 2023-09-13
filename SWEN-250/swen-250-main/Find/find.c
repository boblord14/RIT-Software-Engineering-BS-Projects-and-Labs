/*
 * Implementation of functions that find values in strings.
 *****
 * YOU MAY NOT USE ANY FUNCTIONS FROM <string.h>
 *****
 */

#include <stdlib.h>
#include <stdbool.h>

#include "find.h"

#define NOT_FOUND (-1)	// integer indicator for not found.

/*
 * Return the index of the first occurrence of <ch> in <string>,
 * or (-1) if the <ch> is not in <string>.
 */
int find_ch_index(char string[], char ch) {
	int i;
	for(i=0; string[i]!='\0'; i++){
		if(string[i] == ch){
		return i;
		}
	}
	return NOT_FOUND ;	// placeholder
}

/*
 * Return a pointer to the first occurrence of <ch> in <string>,
 * or NULL if the <ch> is not in <string>.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *find_ch_ptr(char *string, char ch) {
	while(*string!='\0'){
		if(*string == ch){
			return string;
		}
		string++;
	}
	return NULL ;	// placeholder
}

/*
 * Return the index of the first occurrence of any character in <stop>
 * in the given <string>, or (-1) if the <string> contains no character
 * in <stop>.
 */
int find_any_index(char string[], char stop[]) {
	int i;
	for(i=0; string[i]!='\0'; i++){
		for(int j=0; stop[j]!='\0'; j++){
			if (string[i]==stop[j]){
				return i;
			}
		}
	}
	
	return NOT_FOUND ;	// placeholder
}

/*
 * Return a pointer to the first occurrence of any character in <stop>
 * in the given <string> or NULL if the <string> contains no characters
 * in <stop>.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *find_any_ptr(char *string, char* stop) {
	char* originalStop;
	originalStop = stop;
	while(*string!='\0'){
		while(*stop!='\0'){
			if(*string==*stop){
				return string;
			}
			stop++;
		}
		string++;
		stop= originalStop;
	}
	return NULL ;	// placeholder
}

/*
 * Return a pointer to the first character of the first occurrence of
 * <substr> in the given <string> or NULL if <substr> is not a substring
 * of <string>.
 * Note: An empty <substr> ("") matches *any* <string> at the <string>'s
 * start.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *find_substr(char *string, char* substr) {
	char* startLoc;
	char* startLocBackup;
	if(*substr == '\0'){//quick empty string check
		return string;
	}

	startLoc = find_ch_ptr(string, *substr);
	startLocBackup = startLoc;
	if(startLoc == NULL){//if the first char just isnt in the string, its null
		return NULL;
	}
	while(*substr != '\0'){//checking the rest of the chars in the string
		if((*substr != *startLoc) || (*startLoc=='\0')){ //conditions the string wouldnt match under
			return NULL;
		}
		substr++;
		startLoc++;
	}
	return startLocBackup;	//if we make it here its good
}
