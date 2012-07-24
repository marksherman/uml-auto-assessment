/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 29: R, P, S, L and s.           */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
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
  
  for( i = 0 ; i < 5 ; i++ )
    playRound();
  
  return 0;
}                         
int gameLogic(char a, char b)
{
  if( a == rock && b == lizard )
    return 1;
  else if( a == rock && b == scissors )
    return 1;
  else if( b == rock && a == lizard )
    return 2;
  else if( b == rock && a == scissors )
    return 2;
  else if( a == paper && b == rock )
    return 1;
  else if( a == paper && b == spock )
    return 1;
  else if( b == paper && a == rock )
    return 2;
  else if( b == paper && a == spock )
    return 2;
  else if( a == scissors && b == lizard )
    return 1;
  else if( a == scissors && b == paper )
    return 1;
  else if( b == scissors && a == lizard )
    return 2;
  else if( b == scissors && a == paper )
    return 2;
  else if( a == spock && b == scissors )
    return 1;
  else if( a == spock && b == rock )
    return 1;
  else if( b == spock && a == scissors )
    return 2;
  else if( b == spock && a == rock )
    return 2;
  else if( a == lizard && b == spock )
    return 1;
  else if( a == lizard && b == paper )
    return 1;
  else if( b == lizard && a == spock )
    return 2;
  else if( b == lizard && a == paper )
    return 2;
  else
    return draw;


}                                
void playRound()
{

	char p1 = 0, p2 = 0, winner;
	
	printf("\nEnter what was played for both players (r,p,s,l,k): ");

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
