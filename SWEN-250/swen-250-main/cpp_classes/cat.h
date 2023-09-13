#include <string>
#include "animal.h"

#ifndef _cat
#define _cat


class Cat: public Animal
{
public:
    Cat(); //Default ctor
    Cat(std::string a_name);
    void Speak() override;
    void Trick() override;

private:
    std::string name;
    std::string sound;
};
#endif
