# Reverse
n = 27
test : main
	#
	# Should count 50-59. 
	echo "59 58 57 56 55 54 53 52 51 50" | ./a$(n)

main : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
