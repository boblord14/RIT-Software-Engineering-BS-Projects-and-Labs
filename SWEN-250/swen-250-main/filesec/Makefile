# Makefile for filesec Activity
filesec: filesecurity.o main.o
	g++ -g -o filesec filesecurity.o main.o


filesecurity.o: filesecurity.cpp filesecurity.h
	g++ -g -c filesecurity.cpp

main.o: main.cpp
	g++ -g -c main.cpp


test: 
	./tester
clean:
	rm -f filesec *.o a.out *_enc.txt *dec.txt
