This is a copy of my team repo from my personal software engineering class, SWEN-250, from Spring 2023. I took this course alongside SWEN-261(intro to software engineering). 

This class featured indivudal work and served as an intro to C, C++, header files, makefiles,  pointers, memory management, and regex. We worked on our own and submitted labs once a week. 

The fisrt 4/5ths of the class are in C, and range from basic work with C to working with structs, pointers, and pointers of structs. 

The last fifth focused on C++ and regex, with getting used to adapting what we do to the class based system that C++ uses and working with that. 

We used vim, unix, gcc, and command line git to manage everything for our repos and to write/run our labs. 

I got an A in this class. I recieved perfect scores on both midterms and the final exam and perfect scores on almost every one of my labs.

---

Additionally, I have added a second folder to this class's part as this was where I began to get more heavily into reverse engineering. 

The swen-250-decomp folder features two of the reverse engineering extras I did for fun during this class. Both of them have the ghidra project files and the final cleaned up copies of the functions/classes that were decompiled. 

The first one, the project part 1 decomp, was made from pre provided object files used to complete part 2 of our midterm project. 

Compiled object files from a correct copy of the first part were provided for part 2 so if students didn't do part 1 right they could still use a properly functional base to create the second part off of. 

My professor told us "If you can manage to get the code out of this you deserve to be above this class" and I took that as a challenge. Having previous experience with ghidra and due to being straightforward compiled C code with no obfuscation, I was able to reverse engineer out the instructor's code for part 1 of the project. I had already completed part 1 properly for the project so I had an idea of what I was expecting for the output, but it was still a good learning experience. 

The end result of this one was Analysis part 1/2 in the project folder. 

The second folder, the file security one, was a CPP project involving creating a simple encryption, decryption, and byte by byte copy on text and images. We were provided a tester object file to run and confirm that the encryption and decryption functions worked properly.


The byte by byte copy on the image file was extra credit, and had no easy way to test it. So, I used ghidra to reverse engineer the code out of the compiled tester file, add in my own custom test to check the byte by byte copy, recompiled it, and added it back into the project. 

This one was more on a whim because I wanted to make sure I did the work right, and it looked fun to do once more. 

Both of these were excellent intros for me to getting more experienced at decompiling code with ghidra, and working with x86 assembly, both of which I have gotten much better at. 