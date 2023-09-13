#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#define FALSE (0)
#define TRUE  (1)

int main(int ac, char **argv) { /* in goes the args */
	int tot_chars = 0 ;	/* total characters */
	int tot_lines = 0 ;	/* total lines */
	int tot_words = 0 ;	/* total words */
	char input_storage[500]; // arbatrary limit for size	

	int i=0;									//i and ch are vars used in reading in the words for the arg
	int ch ; 
	for (ch = getchar(); ch!= EOF; ch = getchar()){					//just a for loop that takes the arg and puts it in input_storage char by char
		if(i<500){
			input_storage[i++] = ch;
		}
	}	

	int word_terminate = FALSE;							//flag for dumping whitespace or not
	while(input_storage[tot_chars]!='\0' &&  input_storage[tot_chars]!= EOF){ 	//while loop reads the entire input_storage		
		if(isspace(input_storage[tot_chars])){ 					//checks for whitespace
			if(word_terminate == FALSE){ 					
			tot_words++; 							//if we're not ignoring whitespace at the time and theres a whitespace, its a word
			}
			if(input_storage[tot_chars] == '\n'){				//if theres a newline, count another line and prepare to ignore any leading whitespaces next
				tot_lines++;
				word_terminate = TRUE;		
			}
		} else{									//if its not a whitespace you shouldnt be ready to ignore whitespaces in the count
			word_terminate = FALSE;
		}
	       	tot_chars++;								//always add a char for slot in the array
	}
	printf("%d %d %d\n", tot_lines, tot_words, tot_chars);				//standard print output

	return 0 ;
}
