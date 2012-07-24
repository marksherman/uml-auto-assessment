# Sine Function
n = 14
test : main
	#
	###### Should be 1

	./a$(n) 1.57079633

	#
	###### Should be 0
 
	./a$(n) 3.14159265

main : p$(n).c
	gcc -Wall -ansi -lm p$(n).c -o a$(n)
