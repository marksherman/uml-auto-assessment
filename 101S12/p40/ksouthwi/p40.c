/************************************************************/
/* Programmer: Kevin Southwick                              */
/*                                                          */
/* Program 40: Multiple Deterministic Finite State Machines */
/*                                                          */
/* Approximate completion time: 2 hours                     */
/************************************************************/

#include <stdio.h>

int L1_STATE = 0;
int L2_STATE = 0;
int L3_STATE = 0;

void get_state_L1( int input );
void get_state_L2( int input );
void get_state_L3( int input );

int main( int argc , char* argv[] ) {

  char c;
  int x;

  printf( "input a string using the language consisting of { 0 , 1 }. \n" );
  c = getchar();

  while(( c == '0' ) || ( c == '1' ) ){

    /*this loop gets the next character in the string */
    /* and feeds it through the get_state functions   */

    if( c == '0' )
      x = 0;
    else
      x = 1;

    get_state_L1( x );
    get_state_L2( x );
    get_state_L3( x );

    c = getchar();
  }

  /* each of these blocks checks for the accept state */
  /* and prints to std out the accept/reject status   */
  printf( "\nLanguage 1:" );
  if( L1_STATE == 3 )
    printf( " Accepts. \n" );
  else
    printf( " Rejects. \n" );

  printf( "Language 2:" );
  if( L2_STATE == 2 )
    printf( " Accepts. \n" );
  else
    printf( " Rejects. \n" );

  printf( "Language 3:" );
  if( L3_STATE == 1 )
    printf( " Accepts. \n" );
  else
    printf( " Rejects. \n" );

  return 0;

}

void get_state_L1( int input ){
  /* these functions take a look at the current state, and the input */
  /* then moves to the new, appropriate state                        */

  if( ( L1_STATE == 0 ) && ( input == 0 ))
    L1_STATE = 1;
  else if( ( L1_STATE == 1 ) && ( input == 1 ))
    L1_STATE = 2;
  else if( ( L1_STATE == 2 ) && ( input == 0 ))
    L1_STATE = 3;
  else if( ( L1_STATE == 2 ) && ( input == 1 ))
    L1_STATE = 0;

  return;
}

void get_state_L2( int input ){

  if( ( L2_STATE == 0 ) && ( input == 0 ) )
    L2_STATE = 1;
  else if( ( L2_STATE == 0 ) && ( input == 1 ) )
    L2_STATE = 2;
  else if( ( L2_STATE == 1 ) && ( input == 0 ) )
    L2_STATE = 0;
  else if( ( L2_STATE == 1 ) && ( input == 1 ) )
    L2_STATE = 3;
  else if( ( L2_STATE == 2 ) && ( input == 0 ) )
    L2_STATE = 3;
  else if( ( L2_STATE == 2 ) && ( input == 1 ) )
    L2_STATE = 0;
  else if( ( L2_STATE == 3 ) && ( input == 0 ) )
    L2_STATE = 2;
  else if( ( L2_STATE == 3 ) && ( input == 1 ) )
    L2_STATE = 1;

  return;
}

void get_state_L3( int input ){

  if( ( L3_STATE == 0 ) && ( input == 0 ) )
    L3_STATE = 1;
  else if( (L3_STATE == 0 ) && ( input == 1 ) )
    L3_STATE = 3;
  else if( L3_STATE == 1 )
    L3_STATE = 2;
  else if( L3_STATE == 2 )
    L3_STATE = 1;

  return;
}
