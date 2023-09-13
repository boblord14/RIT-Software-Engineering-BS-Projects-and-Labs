#include <string>
#include "animal.h"

#ifndef _parrot
#define _parrot


class Parrot: public Animal
{
public:
    Parrot(); //Default ctor
    Parrot(std::string a_name);
    void Speak() override;
    void Trick() override;

private:
    std::string name;
    std::string sound;
};
#endif
