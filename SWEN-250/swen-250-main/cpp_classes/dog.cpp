#include "dog.h"
#include <iostream>

using namespace std;

Dog::Dog()
{
    //Doesn't do anything
}
Dog::Dog(std::string a_name)
{
    name = a_name;
}

void Dog::Speak()
{
    cout << "The animal " << name << " says Woof" << endl;
}

void Dog::Trick()
{
    cout << "I can roll over" << endl;
}
