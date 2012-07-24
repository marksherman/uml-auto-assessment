# scanf returns what?
n = 21
test : main
	# Should print 101 202 303 404 505 on one line
	#
	./a$(n)

main : p$(n).c
	gcc -Wall -ansi p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#

