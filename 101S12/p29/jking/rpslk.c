/***************************************************************************/
/* Programmer: Jared King                                                */
/*                                                                         */
/* Program 29: Rock Paper Scissors Lizard Spock                            */
/*                                                                         */
/* Approx Completion Time: 30                                              */
/***************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

#define rock 114
#define paper 112
#define scissors 115
#define lizard 108
#define spock 107

const int player1 = 1 ;
const int player2 = 2 ;
const int draw  =   0 ;

void            playRound();
int             gameLogic(char a, char b);

int main(int argc, char *argv[]){
	int i;

	printf("\nRock! Paper! Scissors! Lizard! Spock!\n");


	for( i = 0 ; i < 5 ; i++ )
		playRound();

	return 0;
}

int gameLogic(char a, char b){
       
  switch (a){
  
  case rock:
    if(b==paper||b==spock)
      return player2;
    else if (b==scissors||b==lizard)
      return player1;
    break;

  case paper:
    if(b==scissors||b==lizard)
      return player2;
    else if(b==rock||b==spock)
      return player1;
    break;

  case scissors:
    if(b==spock||b==rock)
      return player2;
    else if(b==paper||b==lizard)
      return player1;
    break;

  case lizard:
    if(b==rock||b==scissors)
      return player2;
    else if(b==paper||b==spock)
      return player1;
    break;

  case spock:
    if(b==paper||b==lizard)
      return player2;
    else if(b==scissors||b==rock)
      return player1;
    break;
  }
  return 0;     
}

void playRound(){

	char p1 = 0, p2 = 0, winner;
	
	printf("\nEnter what was played for both players (r,p,s,l,k): ");

	/* Makes sure we only process alphanumeric chars and ignore others*/
	while( ! isalpha( p1 = getchar() ) );
	while( ! isalpha( p2 = getchar() ) );

	printf("Inputs: %c and %c\n", p1, p2);

	winner = gameLogic(p1, p2);

	if (winner == player1)
		printf("\nPlayer 1 wins!\n");
	else if (winner == player2)
		printf("\nPlayer 2 wins!\n");
	else if (winner == draw)
		printf("\nA Draw! (That's a tie)\n");
	else
		printf("\nUnknown outcome. Problem with gameLogic function\n");

	return;
}
