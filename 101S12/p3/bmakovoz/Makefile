n = 3
main : build test

build : p$(n).c
	gcc -Wall -ansi p$(n).c -g -o a$(n)

test : build
	echo 41 58 | ./a$(n)

clean :
	rm -f a$(n)
	rm -f *~

