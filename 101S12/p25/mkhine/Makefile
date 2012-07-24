# Unfilled Box
n = 25
test : main
	#
	# -1 if they reverse the width and height.
	#
	# Try input like 5 and 3 (this should be 5 wide, 3 high) 
	echo -e "5 3\n" | ./a$(n)
	#
	# Try input 20 and 20 to stress test.
	echo -e "20 20\n" | ./a$(n)
	#
	# Try input 1 and 1 to stress test. THIS MUST WORK
	echo -e "1 1\n" | ./a$(n)
	#
	# Try input 0 and 0 to stress test. (Optional)
	# Should show no output.
	echo -e "0 0\n" | ./a$(n)

main : p$(n).c
	gcc -Wall -ansi p$(n).c -o a$(n)
 
