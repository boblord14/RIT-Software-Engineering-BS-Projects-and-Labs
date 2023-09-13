#include "Card.h"

//Some starter code is provided here -- mainly constructors
//Finish implementing anything required in the constructors and add implementation
//for the remaining methods
//If you want to split this up into multiple files, that is fine.  Make sure you update the Makefile accordingly

Card::Card()
{
    //Initialize to invalid values
    suit = "";
}
Card::~Card()
{
    //Your code here
}
std::string Card::GetSuit() 
{
    return this->suit;
}


PipCard::PipCard(std::string _suit, int number)
{
    this->suit = _suit;
    this->pipNumber = number;
}
PipCard::~PipCard()
{

}
int PipCard::GetNumber()
{
    return this->pipNumber;
}

FaceCard::FaceCard(std::string _name, std::string _suit)
{
    this->name = _name;
    this->suit = _suit;
}
FaceCard::~FaceCard()
{
    //Your code here
}
std::string FaceCard::GetName()
{
    
    return this->name;
}

bool FaceCard::IsAce() 
{
    return this->name == "Ace";
}



