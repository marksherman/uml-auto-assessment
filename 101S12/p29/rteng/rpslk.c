/****************************************************************************/
/* Programmer: Rathanak Teng                                                */
/*                                                                          */
/* Program 29: Rock Paper Scissors Lizard Spock                             */
/*                                                                          */
/* Approx Completion Time: 30 minutes                                       */
/****************************************************************************/

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

int main(int argc, char *argv[])
{
	int i;

	printf("\nRock! Paper! Scissors! Lizard! Spock!\n");

	/* Play 5 rounds in a row (best out of 5?)*/
	for( i = 0 ; i < 5 ; i++ )
		playRound();

	return 0;
}

/* gameLogic 
 * Accepts two characters, representing the forms played by the two players.
 * Returns the winner, which is described by the constant values above.      
 * Inputs must be one of the defined values, and can simply be referenced
 * by name: rock, paper, scissors, lizard, or spock.                         */
int gameLogic(char a, char b)
/*ASSUMES PERFECT USER.
 *Uses switch statements to initially identify the value of player 1, a.
 *For each case, player 2, b, will be evaluated to see if they win/lose/draw to player 1.
 *The respective values will be returned. 
  - 1 if player 1 wins.
  - 2 if player 2 wins.
  - 0 if both player 1 and player 2 have same input, resulting in a draw    
 *Simplified use of inputs to actual hand gesture names.
 *Kept numerical values for win/draw/loss.                                    */
{
  switch(a){
  case rock:
    if ((b == scissors) || (b == lizard))
      return 1;
    else if ((b == paper) || (b == spock))
      return 2;
    else 
      return 0;
  case paper:
    if ((b == rock) || (b == spock))
      return 1;
    else if ((b == scissors) || (b == lizard))
      return 2;
    else
      return 0;
  case scissors:
    if ((b == paper) || (b == lizard))
      return 1;
    else if ((b == rock) || (b == spock))
      return 2;
    else
      return 0;
  case lizard:
    if ((b == paper) || (b == spock))
      return 1;
    else if ((b == rock) || (b == scissors))
      return 2;
    else
      return 0;
  case spock:
    if ((b == rock) || (b == scissors))
      return 1;
    else if ((b == paper) || (b == lizard))
      return 2;
    else
      return 0;
  }
}

/* playRound
 * Plays a single round of the game. 
 * Prompts user, gets data from stdin, and prints results.
 *
 *                DO NOT MODIFY THIS FUNCTION                                */
void playRound()
{

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
