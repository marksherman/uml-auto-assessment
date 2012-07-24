# Recursive Digit Sum
n = 38
test : main
	#
	# Output is 10, 11, 12, 13, 14, 15
	./a$(n) f28

main : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#

fix : 
	ln -s f28 testdata28
