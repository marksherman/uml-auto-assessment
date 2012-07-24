/***************************************************/
/* Programmer: MARTIN KIBUSI                       */
/* 	       	      			           */
/* Program 29: Rock Paper Scissors Lizard Spock	   */
/* 	   				           */
/* Approximate completion time : 45 min            */
/***************************************************/
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
  
  if(a == paper && b == rock){
    return player1;
  }
  else if(a == paper && b == scissors ){
    return player2;
  }
  else if(a == paper && b == lizard){
    return player2;
  }
  else if(a == paper && b == spock){
    return player1;
  }
  else if(a == paper && b == paper){
    return draw;
  }
  if(a == scissors && b == rock){
    return player2;
  }
  else if(a == scissors && b == paper){
   return player1;
 }
  else if(a == scissors && b == lizard){
    return player1;
  }
  else if(a == scissors && b == spock){
    return player2;
  }
  else if(a == scissors && b == scissors){ 
    return draw;
  }
  if( a == spock && b == rock ){
    return player1;
  }
  else if(a == spock && b == paper){
    return player2;
  }
  else if(a == spock && b == scissors){
    return player1;
  }
  else if(a == spock && b == lizard){
    return player2;
  }
  else if(a == spock && b == spock){
    return draw;
  }
  else if(a == lizard && b == rock){
    return player2;
  }
  else if(a == lizard && b == paper){
    return player1;
  }
  else if(a == lizard && b == scissors){
    return player2;
  }
  else if(a == lizard && b == spock){
    return player1;
  }
  else if(a == lizard && b == lizard){
    return draw;
  }
  else if(a == rock && b == paper){
    return player2;
  }
  else if(a == rock && b == scissors){
    return player1;
  }
  else if(a == rock && b == lizard){
    return player1;
  }
  else if(a == rock && b == spock){
    return player2;
  }
  else{
    return draw;
  }
  
}
  
void playRound()
{

  char p1 = 0, p2 = 0, winner;
  
  printf("\nEnter what was played for both players (r,p,s,l,k): ");

  /* Makes sure we only process alphanumeric chars and ignore others*/
  while( ! isalpha( p1 = getchar() ) );
  while( ! isalpha( p2 = getchar() ) );

  printf("Inputs: %c and %c\n", p1, p2);

  winner = gameLogic(p1, p2);

  if (winner == 1)
    printf("\nPlayer 1 wins!\n");
  
  else if (winner == 2)
    printf("\nPlayer 2 wins!\n");
  
  else if (winner == 0)
    printf("\nA Draw! (That's a tie)\n");
  
  else
    printf("\nUnknown outcome. Problem with gameLogic function\n");

  return ;
}

