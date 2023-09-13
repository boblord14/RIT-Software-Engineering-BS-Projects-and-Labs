#include <stdio.h>
#include <iostream>
#include "Test.h"

Tests::Tests()
{
    
}
Tests::~Tests()
{
    
}
int Tests::Count()
{
    return deck.Count();
}

DeckCard* Tests::Peek()
{
    return deck.Peek();
}

bool Tests::EmptyCheck()
{
    int count = deck.Count();
    if (count != 0)
    {
        std::cout << "Empty Check failed: Count was " << count << " expected 0\n";
        return false;
    }
    DeckCard* topCard = deck.Pop();
    if (topCard != nullptr)
    {
        std::cout << "Empty Check failed: Pop was " << topCard << " expected nullptr\n";
        return false;

    }
    //std::cout << "Empty Check passed\n";
    return true;
}

bool Tests::PushOnePip(int expected, int num, std::string suit)
{
    DeckCard* pdc = new DeckCard(num, suit);
    deck.Push(pdc);
    int count = deck.Count();
    if (count != expected)
    {
        std::cout << "After pushing one pip card, count is:" << count << " expecting "
        << expected << std::endl;
        return false;
    }
    return true;
}

bool Tests::PushOneFace(int expected, std::string name, std::string suit)
{
    DeckCard* pdc = new DeckCard(name, suit);
    deck.Push(pdc);
    int count = deck.Count();
    if (count != expected)
    {
        std::cout << "After pushing one face card, count is:" << count << " expecting "
        << expected << std::endl;
        return false;
    }
    return true;
}

DeckCard* Tests::PopOne()
{
    DeckCard* pdc = deck.Pop();
    return pdc;
}



