#include "parrot.h"
#include <iostream>

using namespace std;

Parrot::Parrot()
{
    //Doesn't do anything
}
Parrot::Parrot(std::string a_name)
{
    name = a_name;
}

void Parrot::Speak()
{
    cout << "The animal " << name << " says Wanna cracker -- now!" << endl;
}

void Parrot::Trick()
{
    cout << "My best friend is Captain Jack Sparrow" << endl;
}
