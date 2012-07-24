# fgetc and toupper
n = 23
test : main
	#
	# Should be all caps in three clean lines, with punctuation. 
	# Should not have a blank line after the text.
	./a$(n)

main : p$(n).c
	gcc -Wall -ansi -g p$(n).c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
