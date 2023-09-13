#include "filesecurity.h"
#include <fstream> //File streams

//Default constructor
FileSecurity::FileSecurity()
{
    sourcePath = "";
}


//constructor with param.  Initialize the appropriate variables
FileSecurity::FileSecurity(string filePath)
{
	sourcePath = filePath;
}

//Encode the sourceFile and put result in outputFile
//Return true if successful, return false if there is a problem
//NOTE: ALL bytes in the file must be encoded - even the non-printing ones, such as new-line
bool FileSecurity::Encode()
{
    bool result = false;
    char currentChar;

    ifstream input;
    input.open(sourcePath);

    ofstream output(CreateOutputFileName("enc"));

    if(input.is_open() && output.is_open()){
        currentChar = input.get();
        while(currentChar != EOF){
            output << EncodeByte(currentChar);
            currentChar = input.get();
        }
        result = true;
    } else{
        return result;
    }
    return result;
}

//Decode the sourceFile and put result in outputFile
//Return true if successful, return false if there is a problem
bool FileSecurity::Decode()
{
    bool result = false;
    char currentChar;

    ifstream input;
    input.open(sourcePath);

    ofstream output(CreateOutputFileName("dec"));

    if(input.is_open() && output.is_open()){
        currentChar = input.get();
        while(currentChar != EOF){
            output << DecodeByte(currentChar);
            currentChar = input.get();
        }
        result = true;
    } else{
        return result;
    }
    return result;

}

//extra credit function
//doing a binary copy of a file woohoo
bool FileSecurity::ByteCopy()
{
    bool result = false;

    //read function wants a pointerized char array to write so i need this. length of 1 since reading one at a time
    char* currentByte = new char[0];

    ifstream input;
    input.open(sourcePath, ios::binary);

    ofstream output(CreateOutputFileName("copy"), ios::binary);

    if(input.is_open() && output.is_open()){
        input.read(currentByte, 1);//read one byte to the
        while(!input.eof()){//eof check
            output << currentByte[0];//put byte in output
            input.read(currentByte, 1);//read next
        }
        result = true;
    } else{
        return result;
    }
    return result;
}

//Use the sourcePath to generate the output file name, based on the assignment requirements
//The parameter 'append' is passed in, so the same method can be used for both encoding files
//and decoding files.
string FileSecurity::CreateOutputFileName(string append)
{
    //hacky way to use the legit file type and name properly with the append bit tacked on with a _ right before the end
    string outputFile = sourcePath.substr(0, sourcePath.length()-4) + "_" + append + sourcePath.substr(sourcePath.find("."));
    return outputFile;
}

//Basic encode
//Follow the directions in the requirements to encode a single char/ byte
char FileSecurity::EncodeByte(char c)
{
    return (c + 100);
}

//Basic decode
//Follow the directions in the requirements to decode a single char/ byte

char FileSecurity::DecodeByte(char c)
{
    return (c - 100);
}
