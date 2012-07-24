# argc
n = 13
test : main
	######## Should output 9
	./a$(n) 5 5 5 5 5 5 5 5

main : p$(n).c
	gcc -Wall -ansi -pedantic p$(n).c -g -o a$(n) 

clean :
	rm -f a$(n)
	rm -f *~

