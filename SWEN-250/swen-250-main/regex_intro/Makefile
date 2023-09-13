regex_intro: main.o regextests.o 
	g++ -o regex_intro main.o regextests.o 

main.o: main.cpp regextests.h 
	g++ -c -g main.cpp

regextests.o: regextests.cpp regextests.h
	g++ -c -g -o regextests.o regextests.cpp


clean:
	rm *.o ~* *.bak regex_intro -f
