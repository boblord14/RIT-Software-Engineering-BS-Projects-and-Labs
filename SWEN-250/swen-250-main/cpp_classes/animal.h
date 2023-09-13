#include <string>
#ifndef _animal 
#define _animal
class Animal
{
    public:
    Animal(); //Default ctor
    Animal(std::string a_name, std::string a_sound);
    virtual void Speak();
    virtual void Trick();

    private:
    std::string name;
    std::string sound;
};
#endif