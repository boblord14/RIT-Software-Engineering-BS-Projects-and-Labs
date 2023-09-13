#include <string> //string class
#include <iostream> //cout, cin ...
#include "animal.h"
#include "dog.h"
#include "cat.h"
#include "parrot.h"
#include "giraffe.h"

using namespace std; //so we don't have to say 'std::string' etc.


#define TEST_DOG
#define TEST_CAT
#define TEST_PARROT
#define TEST_GIRAFFE

int main()
{
    Animal generic("blank slate", "silence is golden");
    generic.Speak();
    generic.Trick();
    cout<<endl;

    //i removed the sound param for most of the derived animal classes because thats how i intrepreted the instructions
    //shouldnt be an issue as otherwise its just a copy paste from animal.cpp but i can fix this if need be.
    //instructions just felt a little vague ngl

#ifdef TEST_DOG
    Dog fido("Rover");
    fido.Speak();
    fido.Trick();
    cout<<endl;
    Animal* animal = new Animal("Old Yeller", "Bark and Bite");
    animal->Speak();
    animal->Trick();
    ((Dog*)animal)->Trick();
    delete animal;
    cout<<endl;
#endif

#ifdef TEST_CAT
    Cat whiskers("Selina");
    whiskers.Speak();
    whiskers.Trick();
    cout<<endl;
#endif

#ifdef TEST_PARROT
    Parrot polly("Polly");
    polly.Speak();
    polly.Trick();
    cout << endl;
#endif

#ifdef TEST_GIRAFFE
    Giraffe geoff("Geoff");
    geoff.Speak();
    geoff.Trick();
    cout << endl;
#endif
    return 0;
}

