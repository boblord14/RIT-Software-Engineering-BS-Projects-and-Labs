/*
 * Implementation of functions that filter values in strings.
 *****
 * YOU MAY NOT USE ANY FUNCTIONS FROM <string.h> OTHER THAN
 * strcpy() and strlen()
 *****
 */

#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "filter.h"

#define NUL ('\0')

//didnt use these two given functions. probably could have, just the way i did mine was different and i didnt really need it.
//commented out to remove the compiler warnings that i didnt use them. 

/*
 * YOU MAY FIND THIS FUNCTION USEFUL.
 * Return true if and only if character <c> is in string <s>.
 */
/* static bool includes(char c, char *s) {
	while( *s && c != *s ) {
		s++ ;
	}
	return c == *s ;
}
*/
/*
 * YOU MAY ALSO FIND THIS FUNCTION USEFUL.
 * Return true if and only if string <pre> is a prefix of string <s>.
 */
/*static bool prefix(char *s, char *pre) {
	while( *pre && *s == *pre ) {
		s++ ;
		pre++ ;
	}
	return *pre == NUL ;
}
*/
/*
 * Copy <string> to <result> while removing all occurrences of <ch>.
 */
void filter_ch_index(char string[], char result[], char ch) {
	int resultIndex = 0;	
	for(int i=0; i<strlen(string);i++){
		if(string[i]!=ch){
			result[resultIndex]=string[i];
			resultIndex++;
		}
	}
	result[resultIndex]=NUL;
}

/*
 * Return a pointer to a string that is a copy of <string> with
 * all occurrences of <ch> removed. The returned string must occupy
 * the least possible dynamically allocated space.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *filter_ch_ptr(char *string, char ch){
	char *p_copy;
	p_copy = malloc(strlen(string)+1);
	char *p = p_copy;
	for(int i=0; *string!='\0'; i++){
		if(*string!=ch){
			*p=*string;
			p++;
		}
		string++;
	}
	*p=NUL;
	p=malloc(strlen(p_copy)+1);
	strcpy(p, p_copy);
	free(p_copy);
	return p;
}

/*
 * Copy <string> to <result> while removing all occurrences of
 * any characters in <remove>.
 */
void filter_any_index(char string[], char result[], char remove[]) {
	for(int i=0; remove[i]!=NUL; i++){
		filter_ch_index(string, result, remove[i]);
		string = result;
	}
	if(strlen(remove)==0){
		strcpy(result, string);
	}
	//wrote an indexing version of filter_substr on accident because im an idiot. ignore this
	//ill probably cannibalize parts of the logic for that actual one
	/*
	int substring_instance_flag = 0;
	int result_index = 0;
	for(int i=0; string[i]!='\0';i++){
		if(string[i]==remove[0]){
			for(int j=0; remove[j]!='\0'; j++){//verify complete instance of substring
				substring_instance_flag = 1;
				if(string[i+j]!=remove[j]){
					substring_instance_flag = 0;
					break;
				}
			}
		}
		if(substring_instance_flag == 1){
			i+=strlen(remove);
			substring_instance_flag = 0;
		} else {
			result[result_index] = string[i];
			result_index++;
		}
	}
	result[result_index] = NUL;
	*/
}

/*
 * Return a pointer to a copy of <string> after removing all
 * occurrrences of any characters in <remove>.
 * The returned string must occupy the least possible dynamically
 * allocated space.
 *****
 * YOU MAY *NOT* USE INTEGERS OR ARRAY INDEXING.
 *****
 */
char *filter_any_ptr(char *string, char* remove) {
	char *temp_result;
	temp_result = malloc(strlen(string)+1);
	char *copy_temp = temp_result;
	strcpy(temp_result, string);
	for(int i=0; *remove!=NUL; i++){
		char *filter_temp_string = filter_ch_ptr(copy_temp, *remove);
		strcpy(temp_result, filter_temp_string);
		free(filter_temp_string);
		copy_temp = temp_result;//is this necessary? dont know but id rather be safe than sorry
		remove++;
	}
	//filter_chr_ptr should null terminate by default
	copy_temp = NUL;
	copy_temp = malloc(strlen(temp_result)+1);
	strcpy(copy_temp, temp_result);
	free(temp_result);
	return copy_temp;
}

/*
 * Return a pointer to a copy of <string> after removing all
 * occurrrences of the substring <substr>.
 * The returned string must occupy the least possible dynamically
 * allocated space.
 *****
 * YOU MAY *NOT* USE ARRAY INDEXING.
 *****
 */
char *filter_substr(char *string, char* substr) {
	char *char_index = substr;
	int substring_instance_flag = 0;
	char *result = malloc(strlen(string)+1);
    char *result_copy = result;
	while(*string!=NUL){
		if(*string==*char_index){
			substring_instance_flag = 1;
			char *substr_test = string;
			while(*char_index!=NUL){//verify complete instance of substring
				if(*substr_test!=*char_index){
					substring_instance_flag = 0;
					char_index = substr;
					break;
				}
				substr_test++;
				char_index++;
			}
		}
		if(substring_instance_flag == 1){
			string+=strlen(substr);
            char_index = substr;
			substring_instance_flag = 0;
		} else {
			*result_copy = *string;
			result_copy++;
			string++;
		}
	}
	*result_copy = NUL;
    result_copy = malloc(strlen(result)+1);
    strcpy(result_copy, result);
    free(result);
    return result_copy;
}
