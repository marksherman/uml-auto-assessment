n = 29
# Rock Paper Scissors Lizard Spock
test : build

	#############################
	#####################Player 1
	#############################
	# Player 1 wins all of these:
	echo "sp pr rl lk ks" | ./a$(n)
	echo "sl lp pk kr rs" | ./a$(n)

	#############################
	#####################Player 2
	#############################
	# Player 2 wins all of these:
	echo "ps rp lr kl sk" | ./a$(n)
	echo "ls pl kp rk sr" | ./a$(n)

	#############################
	#########################DRAW
	#############################
	# All draws:
	echo "rr pp ss ll kk" | ./a$(n)

fix :
	ln -sf p$(n).c rpslk.c 

build : rpslk.c
	gcc -Wall -ansi -g rpslk.c -o a$(n)

clean :
	rm -f a$(n)
	rm -f *~
	rm -f \#*\#
