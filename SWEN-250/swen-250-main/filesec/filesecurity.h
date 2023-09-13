#include <stdio.h>
#include <iostream>
#include <string.h>

using namespace std;

#ifndef _filesec_
#define _filesec_

class FileSecurity
{
    public:
        FileSecurity();
        FileSecurity(string filePath);
        bool Encode();
        bool Decode();
	bool ByteCopy();
    protected:
        string sourcePath;
    private:
        char EncodeByte(char c);
        char DecodeByte(char c);
        string CreateOutputFileName(string append);
};

#endif
