#include "Card.h"

//Deck is a LIFO stack using a linked list

class DeckCard
{
    public:
    DeckCard(int number, std::string suit);//Constructor for PipCard
    DeckCard(std::string name, std::string suit);//Constructor for FaceCard
    ~DeckCard();
    DeckCard* pNextCard;//Points to the next card; nullptr if no next card
    Card* pCard; //A card
};

class Deck
{
    public:
    Deck();
    ~Deck();
    void Push(DeckCard* pCard); //Pushes a card to top of stack
    DeckCard* Pop(); //Returns top card and moves ptr head to next card
    DeckCard* Peek(); //Returns top card; ptr head doesn't change

    int Count(); //Returns count of cards in the deck
    protected:
    DeckCard* pCardListHead; //Head of the list.  No explicit tail -- have to use 'next'

};

