n = 2

test : build
	echo 99 | ./a$(n)

build : p$(n).c
	gcc -Wall -ansi p$(n).c -g -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~

