# Equal to zero?
n = 6

test : build
	#####################################
	###### Should NOT be equal to zero

	echo -1 | ./a$(n)

	#

	echo 1 | ./a$(n)

	#

	#####################################
	###### Should be equal to zero

	echo 0 | ./a$(n)
	#

build : p$(n).c
	gcc -Wall -ansi p$(n).c -g -o a$(n)
	chgrp 91101ta a$(n)
	chmod 770 a$(n)

clean :
	rm -f a$(n)
	rm -f *~
