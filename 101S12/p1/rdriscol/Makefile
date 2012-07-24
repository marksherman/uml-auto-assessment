main : testa testb testc

testa : a
	./aa
	#
	#

testb : b
	./ab
	#
	#

testc : c
	./ac
	#
	#

a : p1a.c
	# Program A: Hello World
	gcc -Wall -ansi -g p1a.c -o aa

b : p1b.c
	# Program B: Value 6
	gcc -Wall -ansi -g p1b.c -o ab

c : p1c.c
	# Program C: Character P
	gcc -Wall -ansi -g p1c.c -o ac

clean :
	rm -f a*
	rm -f *~
	rm -f \#*\#
