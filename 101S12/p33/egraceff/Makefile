n = 33
# Recursive Factorial 

test : build
	#
	# Should output 
	# 362880
	# 40320
	# 120
	# 1
	# 
	echo "9" | ./a$(n)
	echo "8" | ./a$(n)
	echo "5" | ./a$(n)
	echo "0" | ./a$(n)

build : p$(n).c
	gcc -Wall -ansi p$(n).c -g -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
