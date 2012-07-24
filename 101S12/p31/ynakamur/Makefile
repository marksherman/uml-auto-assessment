n = 31
# Inner Product of Two Vectors
test : build
	#
	# Should output 300.500000
	#                                                                      
	echo "2.2 3.2 5 7.5 999 13 17 19 20 9.375 11 3.8 0 4 2 3" | ./a$(n)

build : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
