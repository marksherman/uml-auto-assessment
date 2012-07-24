# Reverse the Command Line
n = 20
test : main
	#
	# Should print 3 2 1 ./blastoff on separate lines
	#
	./blastoff 1 2 3

main : p$(n).c
	gcc -Wall -ansi -pedantic p$(n).c -o blastoff

clean :
	rm -f blastoff
	rm -f *~
	rm -f \#*\#
