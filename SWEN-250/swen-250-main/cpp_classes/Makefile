test: main.o animal.o dog.o cat.o parrot.o giraffe.o
	g++ -g -o test main.o animal.o dog.o cat.o parrot.o giraffe.o

animal.o: animal.cpp animal.h
	g++ -g -c animal.cpp

dog.o: dog.cpp dog.h
	g++ -g -c dog.cpp

cat.o: cat.cpp cat.h
	g++ -g -c cat.cpp

parrot.o: parrot.cpp parrot.h
	g++ -g -c parrot.cpp

giraffe.o: giraffe.cpp giraffe.h
	g++ -g -c giraffe.cpp

main.o: main.cpp
	g++ -g -c main.cpp


#Example below of adding the class 'dog'.  Each time you add a new file, you would change the Makefile
#test: main.o animal.o dog.o 
#	g++ -g -o test main.o animal.o dog.o
#dog.o: dog.cpp dog.h
#	g++ -g -c dog.cpp


clean:
	rm -f test test.exe
	rm -f *.o *.bak *~*
