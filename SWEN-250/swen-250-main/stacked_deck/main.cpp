#include <iostream>
#include "Test.h"

int main(int argc, char** argv)
{
    Tests myTest;
    bool testResult = false;
    int count = -1;
    //Test empty list exists
    //Test that you can't pop an empty list
    //Test that count of empty list is zero
    std::cout << "Test 1: Testing empty stack, popping empty stack, count should stay 0\n";
    testResult = myTest.EmptyCheck();
    if (!testResult)
        return -1;
    testResult = myTest.PopOne(); //Should return false
    if (testResult)
        return -1;
    count = myTest.Count();
    if (count != 0)
        return -1;
    std::cout << "Test 1: passed\n";

    //Push two and check top and count of two
    std::cout << "Test 2: Pushing two cards; checking count\n";
    testResult = myTest.PushOnePip(1, 5, "Diamonds"); 
    if (!testResult)
        return -1;
    testResult = myTest.PushOneFace(2, "Jack", "Hearts");
    if (!testResult)
        return -1;
    count = myTest.Count();
    //Count should now be 2
    if (count != 2)
    {
        std::cout << "Expected count of 2; got " << count <<std::endl;
        return -1;
    }
    DeckCard* peek = myTest.Peek();
    //count should still be 2
    FaceCard* pFace = (FaceCard*)peek->pCard;
    if ( pFace->GetName() != "Jack" || pFace->GetSuit() != "Hearts"
        || myTest.Count() != 2)
    {
        std::cout << "Peek test failed\n";
    }

    std::cout << "Test 2: passed\n";
    std::cout <<"Test 3: Popping each and check values and count\n";
    DeckCard* pDc = myTest.PopOne();
    //Should be 2nd one pushed (Jack of Hearts)
    pFace = (FaceCard*)peek->pCard;
    if ((pFace->GetName() != "Jack") || (pFace->GetSuit() != "Hearts")
        || pDc->pCard == nullptr)
    {
        std::cout << "PopOne: Expected Jack of Hearts; got " <<
        pFace->GetName() << "/" << pFace->GetSuit() << std::endl;
        return -1;
    }
    delete pDc;//Free memory
    pDc = myTest.PopOne(); //Pop the next one
    PipCard* pPip = (PipCard*)pDc->pCard;
    //Should be 1st one pushed (5 of Diamonds)
    if ((pPip->GetNumber() != 5) || (pPip->GetSuit() != "Diamonds")
        || pDc->pCard == nullptr)
    {
        std::cout << "PopOne: Expected 5 of Diamonds; got " <<
        pPip->GetNumber() << "/" << pPip->GetSuit() << std::endl;
        return -1;
    }
    delete pDc;
    testResult = myTest.EmptyCheck();
    if (testResult == false)
    {
        std::cout << "After 2 push/ 2 pop, expected empty list\n";
    }
    std::cout << "Test 3: passed\n";


    //Push 1 with ace at top and make sure isAce is set
    //Pop it and make sure everything is empty
    std::cout<<"Test 4: Pushing Ace; checking values and isAce\n";
    testResult = myTest.PushOneFace(1, "Ace", "Spades");
    if (!testResult)
        return -1;
    pDc = myTest.PopOne();
    FaceCard* pFaceCard = (FaceCard*)pDc->pCard;
    if ((pFaceCard->GetName() != "Ace") ||
        (pFaceCard->GetSuit() != "Spades") ||
        (pFaceCard->IsAce() == false) )
    {
        std::cout << "Expected Ace of Spades\n";
        std::cout << "Test 4: failed\n";
        return -1;
    }
    count = myTest.Count();
    if (count != 0)
    {
        std::cout << "Stack should be empty\n";
        return -1;
    }
    delete pDc;
    std::cout<<"Test 4: passed\n";

    return 0;
}

