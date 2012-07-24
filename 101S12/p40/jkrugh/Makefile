test : main
	#
	# Should Accept, Accept, Reject, Reject on all 3 languages
	# Followed by R, R, A
	#
	echo "010" | ./ah2
	echo "001001001" | ./ah2
	echo "0111" | ./ah2
	echo "111000" | ./ah2
	#***********************************************************
	echo "0" | ./ah2

main : p40.c
	gcc -Wall -ansi -g p40.c -o ah2

clean :
	rm -f ah2
	rm -f *~
	rm -f \#*\#