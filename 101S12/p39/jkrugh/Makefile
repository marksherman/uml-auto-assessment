n = 39
# Fully Recursive Persistence of a Number
test : main
	#
	# Should output 0, 1, 2, 3, 4
	#
	echo "9 71 35 726 999" | ./a$(n)

main : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
