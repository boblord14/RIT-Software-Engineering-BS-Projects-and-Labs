SOURCES = main.cpp Test.cpp DMVSchedule.cpp Customer.cpp ServiceLine.cpp
HEADERS = Test.h DMVSchedule.h Customer.h ServiceLine.h
OBJS = $(SOURCES:.cpp=.o)

# Primary target - test_dmv_schedule executable

test_dmv_schedule: $(OBJS)
	g++ -o test_dmv_schedule -ggdb $(OBJS) 

# Object file dependencies.
# See .cpp.o rule for how Cpp files are converted to object files implicitly.

Test.o: Test.cpp Test.h

DMVSchedule.o: DMVSchedule.cpp DMVSchedule.h

Customer.o: Customer.cpp Customer.h

ServiceLine.o: ServiceLine.cpp ServiceLine.h

main.o: main.cpp

# Clean up the directory

clean:
	rm -f test_dmv_schedule 
	rm -f *.o *.bak *~*

# Implicit rule for converting .cpp files to .o files when the
# .o file is out of date.

.cpp.o:
	g++ -c -g $<
