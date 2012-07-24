test : main
	ln -fs testdata8-1 testdata8
	###### Should output 1 *
	./a8
	ln -fs testdata8-30 testdata8
	###### Should output 1  1  31
	./a8 | wc
main : p8.c
	gcc -Wall -ansi -pedantic p8.c -o a8
