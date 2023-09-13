test: test.o deck.o card.o main.o
	g++ -g -o test test.o deck.o card.o main.o

main.o: main.cpp
	g++ -g -c main.cpp

test.o: Test.cpp Test.h Deck.cpp Deck.h
	g++ -g -c -o test.o Test.cpp

deck.o: Deck.cpp Deck.h Card.cpp Card.h
	g++ -g -c -o deck.o Deck.cpp

card.o: Card.cpp Card.h
	g++ -g -c -o card.o Card.cpp

clean:
	rm -rf *.o test