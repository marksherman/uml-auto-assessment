#Using the sqrt function
n = 12
test : main
	#
	###### Should be 10

	echo 100 | ./a$(n)

	#
	###### Should be 45.45 (with a bit of rounding error)
 
	echo 2065.7025 | ./a$(n)

main : p$(n).c
	gcc -Wall -ansi -pedantic -lm p$(n).c -o a$(n)
