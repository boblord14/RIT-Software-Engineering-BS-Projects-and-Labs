#include "animal.h"
#include <iostream>

using namespace std;

Animal::Animal()
{
    //Doesn't do anything
}
Animal::Animal(std::string a_name, std::string a_sound)
{
        name = a_name;
        sound = a_sound;
}

void Animal::Speak()
{
    cout << "The animal " << name << " says " << sound << endl;
}

void Animal::Trick()
{
    cout << "Teach me a trick" << endl;
}