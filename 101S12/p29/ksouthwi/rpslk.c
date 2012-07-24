/****************************************************************************/
/* Programmer: Kevin Southwick (gameLogic) & Mark Sherman (everything else) */
/*                                                                          */
/* Program 29: Rock Paper Scissors Lizard Spock                             */
/*                                                                          */
/* Approx Completion Time:  20 minutes + what Mark spent                    */
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
{
  if( a == spock ){
    if( ( b == rock ) || ( b == scissors )) /*win conditions*/
      return player1;
    else if( b == a ) /*draw conditions*/
      return draw;
    else if ( ( b == paper ) || ( b == lizard )) /*lose conditions*/
      return player2;
    else
      return EOF;  /*if invalid char is read in, EOF results*/
  }  
  else if( a == paper ){
    if( ( b == rock ) || ( b == spock ))
      return player1;
    else if( b == a )
      return draw;
    else if ( ( b == scissors ) || ( b == lizard ))
      return player2;
    else
      return EOF;
  }
  else if( a == scissors ){
    if( ( b == paper ) || ( b == lizard ))
      return player1;
    else if( b == a )
      return draw;
    else if ( ( b == rock ) || ( b == spock ))
      return player2;
    else
      return EOF;
  }
  else if( a == rock ){
    if( ( b == lizard ) || ( b == scissors ))
      return player1;
    else if( b == a )
      return draw;
    else if ( ( b == paper ) || ( b == spock ))
      return player2;
    else
      return EOF;
  }
  else if( a == lizard ){
    if( ( b == spock ) || ( b == paper ))
      return player1;
    else if( b == a )
      return draw;
    else if ( ( b == rock ) || ( b == scissors ))
      return player2;
    else
      return EOF;
  }
  else return EOF; /* if invalid char is read in, EOF results */
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
