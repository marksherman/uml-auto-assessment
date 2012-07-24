n = 30
# Simulating Call by Reference
test : build
	#
	# Should output 99 then 55
	#
	echo "55 99" | ./a$(n)

build : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
