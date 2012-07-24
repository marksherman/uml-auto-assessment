# Area of a rectangle
n = 17
test : main
	#
	# Should be just under 25 (24.999987)
	#
	echo "4.25 5.88235" | ./a$(n)

main : p$(n).c
	gcc -Wall -ansi p$(n).c -o a$(n)

clean : 
	rm -rf a$(n)
