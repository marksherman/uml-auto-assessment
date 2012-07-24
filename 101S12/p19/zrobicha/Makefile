# argv
n = 19
test : main
	# Prints "a$(n) Apple Boat Camera" all on separate lines
	./a$(n) Apple Boat Camera

main : p$(n).c
	gcc -Wall -ansi -pedantic p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#