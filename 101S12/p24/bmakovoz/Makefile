# Find the Average
n = 24
test : main
	#
	# Should output 18.750000
	./a$(n)

main : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#

