# Positive, Negative, or Zero?
n = 7

test : build
	#####################################
	###### Should be positive

	echo 1 | ./a7

	#####################################
	###### Should be negative
 
	echo -1 | ./a7

	#####################################
	###### Should be zero

	echo 0 | ./a7
	#####################################

build : p$(n).c
		gcc -Wall -ansi p$(n).c -g -o a$(n)
		chgrp 91101ta a$(n)
		chmod 770 a$(n)

clean :
	rm -f a$(n)
	rm -f *~
