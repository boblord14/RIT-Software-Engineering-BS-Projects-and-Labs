#include "cat.h"
#include <iostream>

using namespace std;

Cat::Cat()
{
    //Doesn't do anything
}
Cat::Cat(std::string a_name)
{
    name = a_name;

}

void Cat::Speak()
{
    cout << "The animal " << name << " says Meow" << endl; //slightly diff than the sample output but whatever
}

void Cat::Trick()
{
    cout << "I play with bats" << endl;
}
