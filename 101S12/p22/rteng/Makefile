# Sum of a Bunch
n = 22
test : main
	#
	# Should output exactly 1000
	./a$(n)

main : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#

