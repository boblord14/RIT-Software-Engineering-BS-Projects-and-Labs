#include "Deck.h"

//Starter code.  Add anything you need.  Add remaining methods.
DeckCard::DeckCard(int number, std::string suit)
{
   pCard = new PipCard(suit, number);
   pNextCard = NULL;
}

DeckCard::DeckCard(std::string name, std::string suit)
{
    pCard = new FaceCard(name, suit);
}
DeckCard::~DeckCard()
{
delete pCard;
}

Deck::Deck()
{
    pCardListHead = nullptr;
//Add any additional code
}

Deck::~Deck()
{

}	
void Deck::Push(DeckCard* pCard) //Pushes a card to top of stack
{
    pCard->pNextCard = pCardListHead;
    pCardListHead = pCard;
}

DeckCard* Deck::Pop() //Returns top card and moves ptr head to next card
{
    DeckCard *top = pCardListHead;
    if(top != nullptr){
        pCardListHead = pCardListHead->pNextCard;
    }
    return top;
}
    
//Returns top card; ptr head doesn't change
DeckCard* Deck::Peek()
{
    return pCardListHead;
} 

//Returns count of cards in the deck
int Deck::Count()
{
    DeckCard *currentCard = pCardListHead;
    int cardCount = 0;
    while(currentCard != NULL){
        cardCount++;
        currentCard = currentCard->pNextCard;
    }
    return cardCount;
} 
 
