test : main
	###### Should be NOT bigger than 100
	echo 99 | ./a5
	#
	echo 100 | ./a5
	#
	###### Should be bigger than 100
	echo 101 | ./a5
	#
	#####################################

main : p5.c
	gcc -Wall -ansi p5.c -o a5
	chgrp 91101ta a5
	chmod 770 a5
