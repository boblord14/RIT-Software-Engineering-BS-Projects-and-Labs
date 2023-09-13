test:	main.o linked_list_functions.o unit_tests.o analysis.o unit_tests_linked_list_functions.o unit_tests_analysis.o unit_tests_student.o
	gcc -o test -g main.o linked_list_functions.o unit_tests.o analysis.o unit_tests_linked_list_functions.o unit_tests_analysis.o unit_tests_student.o
	
linked_list_functions.o: save_object
	cp save_object linked_list_functions.o

main.o:	main.c unit_tests.h 
	gcc -c -g -Wall main.c
	
unit_tests.o:	unit_tests.c unit_tests.h
	gcc -c -g -Wall unit_tests.c
	
unit_tests_analysis.o:	unit_tests_analysis.c unit_tests.h analysis.h
	gcc -c -g -Wall unit_tests_analysis.c
	
unit_tests_student.o:	unit_tests_student.c unit_tests.h analysis.h
	gcc -c -g -Wall unit_tests_student.c
	
unit_tests_linked_list_functions.o:	unit_tests_linked_list_functions.c unit_tests.h linked_list_functions.h
	gcc -c -g -Wall unit_tests_linked_list_functions.c
	
analysis.o:	analysis.c linked_list_functions.h analysis.h
	gcc -c -g -Wall analysis.c

clean:
	rm -rf *.o test
	
test_student: main.o linked_list_functions.o unit_tests.o analysis.o unit_tests_linked_list_functions.o unit_tests_analysis.o unit_tests_student.o
	make clean
	make -f Makefile_students_part1