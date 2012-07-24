# Solid box of asterisks
n = 15

test : main
	#
	# Stress testing low: 1 x 1
	echo 1 1 |./a$(n)
	#
	# Stress testing high: 20 x 20
	echo 20 20 |./a$(n)
	# Should print 20 + however many lines of input/output at top of program
	echo 20 20 |./a$(n) | wc -l

main : p$(n).c
	gcc -Wall -ansi p$(n).c -o a$(n)

clean :
	rm a$(n)
