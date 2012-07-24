n = 4
main : build test

build : p$(n).c
	gcc -Wall -ansi p$(n).c -g -o a$(n)

test : build
	./a$(n)

clean :
	rm -f a$(n)
	rm -f *~
