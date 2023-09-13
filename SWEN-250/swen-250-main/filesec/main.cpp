#include <stdio.h>
#include <iostream>

#include "filesecurity.h"
using namespace std;

//Your code in main will read the command line arguments, and based on the parameters
//either process the file, or detect an error in the arguments and print usage instructions.
int main(int argc, char** argv){
   if(argc == 3){
     if(strcmp(argv[1], "-e") == 0){
         FileSecurity encode(argv[2]);
         encode.Encode();
     } else if(strcmp(argv[1], "-d") == 0){
         FileSecurity decode(argv[2]);
         decode.Decode();
     }else if(strcmp(argv[1], "-c") == 0) {//extra credit function
         FileSecurity copy(argv[2]);
         copy.ByteCopy();
     }else{
        cout << "Usage:\nfilesec -e|-d|-c [filename]";
     }
    } else{
        cout << "Usage:\nfilesec -e|-d|-c [filename]";
    }
    return 0;
}
