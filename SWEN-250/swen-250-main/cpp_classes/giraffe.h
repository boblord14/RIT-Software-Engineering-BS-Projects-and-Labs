#include <string>
#include "animal.h"

#ifndef _giraffe
#define _giraffe


class Giraffe: public Animal
{
public:
    Giraffe(); //Default ctor
    Giraffe(std::string a_name);
    void Speak();
    void Trick() override;

private:
    std::string name;
    std::string sound;
};
#endif
