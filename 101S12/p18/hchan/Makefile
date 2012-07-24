# Area of a circle
n = 18

test : main
	#
	# Should be 50
	#
	echo "3.989422804014327" | ./a$(n)

main : p$(n).c
	gcc -Wall -ansi -D_GNU_SOURCE p$(n).c -o a$(n)

clean : 
	rm -rf a$(n)
