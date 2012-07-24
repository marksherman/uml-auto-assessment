test : build
	valgrind -q ./a.out

build : linked_list.c linked_list.h p44.c
	gcc -Wall -ansi -g p44.c linked_list.c -o a.out

clean :
	rm -f a.out
	rm -f *~
	rm -f \#*\#