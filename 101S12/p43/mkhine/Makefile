n = 43
# Square Deal
test : build
	#
	# Should 
	#
	echo "3 100" | ./a$(n) 
	echo "15 1" | ./a$(n)

build : p$(n).c
	gcc -Wall -ansi -lm -O p$(n).c -o a$(n)

debug : p$(n).c
	gcc -Wall -ansi -lm -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
	rm -f test*
