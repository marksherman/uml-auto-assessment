# The abs function
test : main
	#
	###### Should be positive

	echo 1 | ./a.out

	#
	###### Should be positive
 
	echo -1 | ./a.out

	#	
	###### Should be zero

	echo 0 | ./a.out

main : p11.c
	gcc -Wall -ansi p11.c