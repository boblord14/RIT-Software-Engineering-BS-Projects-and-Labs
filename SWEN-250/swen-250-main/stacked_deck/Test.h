#include <string>
#include "Deck.h"

class Tests
{
    public:
    Tests();
    ~Tests();
    bool EmptyCheck();
    bool PushOnePip(int expectedCount, int num, std::string suit);
    bool PushOneFace(int expectedCount, std::string name, std::string suit);
    DeckCard* PopOne();
    DeckCard* Peek();
    int Count();
    private:
    Deck deck;
};
