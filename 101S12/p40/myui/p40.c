/************************************************************/
/* Programmer: Ming Yui Chung Jacky                         */
/*                                                          */
/* Program : Multiple Deterministic FSM                     */
/*                                                          */
/* Approximate completion time: 3hrs                        */
/************************************************************/

#include<stdio.h>

int language1( char digit, int state );
int language2( char digit, int state );
int language3( char digit, int state );

int main( int argc, char *argv[] ) {

  int lan1state, lan2state, lan3state;
  char digit;

  lan1state = 0;
  lan2state = 0;
  lan3state = 0;

  printf( "Please enter a string of characters: " );
  
  /* Get one symbol each time and check with each language. */
  while( ( digit = getchar() ) != '\n' ) {
   
    lan1state = language1( digit, lan1state );
    lan2state = language2( digit, lan2state );
    lan3state = language3( digit, lan3state );

  }
  
  /* Print out whether accepts or not. */
  if ( lan1state == 3 )
    printf( "Language 1 accepts.\n" );
  else
    printf( "Language 1 rejects.\n" );

  if ( lan2state == 2 )
    printf( "Language 2 accepts.\n" );
  else
    printf( "Language 2 rejects.\n" );

  if ( lan3state == 2 ) 
    printf( "Language 3 accepts.\n" );
  else 
    printf( "Language 3 rejects.\n" );

  return 0;
}

/* Digit check for language 1 */
int language1( char digit, int state ) {

  if ( ( state == 0 ) & ( digit == '0' ) )
    state = 1;
  else if ( ( state == 1 ) & ( digit == '1' ) )
    state = 2;
  else if ( ( state == 2 ) & ( digit == '0' ) )
    state = 3;
  else if ( ( state == 2 ) & ( digit == '1' ) )
    state = 0;
  
  return state;

}

/* Digit check for language 2 */
int language2( char digit, int state ) {

  if ( ( state == 0 ) & ( digit == '0' ) )
    state = 1;
  else if ( ( state == 0 ) & ( digit == '1' ) )
    state = 2;

  else if ( ( state == 1 ) & ( digit == '0' ) )
    state = 0;
  else if ( ( state == 1 ) & ( digit == '1' ) )
    state = 3;

  else if ( ( state == 2 ) & ( digit == '0' ) )
    state = 3;
  else if ( ( state == 2 ) & ( digit == '1' ) )
    state = 0;

  else if ( ( state == 3 ) & ( digit == '0' ) )
    state = 2;
  else if ( ( state == 3 ) & ( digit == '1' ) )
    state = 1;

  return state;

}

/* Digit check for language 3 */

int language3( char digit, int state ) {

  if ( ( state == 0 ) & ( digit == '0' ) )
    state = 1;
  else if ( ( state == 0 ) & ( digit == '1' ) )
    state = 4;
  if ( ( state == 1 ) & ( digit == '1') )
    state = 2;

  return state;

}
