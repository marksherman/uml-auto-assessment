n = 41
# Malloc up space for a 1-Dimensional array of n integers
test : main
	# Prints 630
	cat input | ./a$(n)

main : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
