#include <string>
#include "animal.h"

#ifndef _dog
#define _dog


class Dog: public Animal
{
public:
    Dog(); //Default ctor
    Dog(std::string a_name);
    void Speak() override;
    void Trick() override;

private:
    std::string name;
    std::string sound;
};
#endif
