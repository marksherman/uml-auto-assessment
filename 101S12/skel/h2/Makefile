test : build
	echo "abc" | ./ah2
	echo "foo bar" | ./ah2
	echo "hello world" | ./ah2

build : h2.c
	gcc -Wall -ansi -g h2.c -o ah2

clean :
	rm -f ah2
	rm -f *~
	rm -f \#*\#