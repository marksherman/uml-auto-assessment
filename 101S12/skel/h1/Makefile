test : build
	./ah1

build : h1.c
	gcc -Wall -ansi h1.c -o ah1

pdf : h1.c
	a2ps h1.c -o - | ps2pdf - h1.pdf
