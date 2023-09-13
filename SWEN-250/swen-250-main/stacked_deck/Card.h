#include <string>

/*
Type Definitions:
PipCard: 2-10 cards, any suit
FaceCard: Jack, King, Queen (or Ace), any suit
*/
class Card
{
    public:
    Card();
    virtual ~Card();
    std::string GetSuit();
    protected:
    //Set appropriate variable depending on type of card
    std::string suit;//Clubs, Diamonds, etc
};

//PipNumber would be 1->10
class PipCard: public Card
{
    public:
    PipCard( std::string _suit, int num); //Special constructor for PipCard
    ~PipCard();
    int GetNumber();
    protected:
    int pipNumber;
};

//Use for Face cards including Ace; special handling if it is an Ace
//Name would be 'king, jack,' etc
class FaceCard: public Card
{
    public:
    FaceCard(std::string _name, std::string _suit); //Special constructor for FaceCard
    ~FaceCard();
    std::string GetName();
    bool IsAce();
    protected:
    //True iff name is 'Ace'
    bool isAce;
    std::string name;
};


