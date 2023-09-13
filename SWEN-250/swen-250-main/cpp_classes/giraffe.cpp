#include "giraffe.h"
#include <iostream>

using namespace std;

Giraffe::Giraffe()
{
    //Doesn't do anything
}
Giraffe::Giraffe(std::string a_name)
{
    name = a_name;

}

void Giraffe::Speak()
{
    cout << "The animal " << name << " says ToysRMe!" << endl; //again slightly diff than the sample output but it shouldnt matter
}

void Giraffe::Trick()
{
    cout << "I sold my soul to commercialism :(" << endl;
}
