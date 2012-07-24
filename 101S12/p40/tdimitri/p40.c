/***********************************************************************/
/* Programmer: Theodore Dimitriou                                      */
/* Program 40: Multiple Deterministic Finite State Machines            */
/* Approximate completion time: 4:30 hours                             */
/***********************************************************************/

#include <stdio.h>
#include <stdlib.h>

int L1( char c );
int L2( char c );
int L3( char c );

int main( int argc, char* argv[] ){
  
  char c;
  
  printf( "Please enter a string of characters:\n" );
  
  c = getchar();
  
  while( c != EOF ){
    L1( c );
    L2( c );
    L3( c );
    c = getchar();
  }
  
  if( L1( c ) == 1 )
    printf( "\nLanguage 1 accepts.\n" );
  else
    printf( "\nLanguage 1 rejects.\n" );
  if( L2( c ) == 1 )
    printf( "Language 2 accepts.\n" );
  else
    printf( "Language 2 rejects.\n" );
  if( L3( c ) == 1 )
    printf( "Language 3 accepts.\n\n" );
  else
    printf( "Language 3 rejects.\n\n" );
  
  return 0;
}

int L1( char c )
{
  static int state = 0;
  
  if( ( state == 0 ) && ( c == '0' ) )
    state = 1;
  else if( ( state == 0 ) && ( c == '1' ) )
    state = 0;
  else if( ( state == 1 ) && ( c == '0' ) )
    state = 1;
  else if( ( state == 1 ) && ( c == '1' ) )
    state = 2;
  else if( ( state == 2 ) && ( c == '0' ) )
    state = 3;
  else if( ( state == 2 ) && ( c == '1' ) )
    state = 0;
  else if( ( state == 3 ) && ( c == '0' ) )
    state = 3;
  else if( ( state == 3 ) && ( c == '1' ) )
    state = 3;
  
  if( state == 3 )
    return 1;
  else
    return 0;
}

int L2( char c )
{
  static int state = 0;
  
  if( ( state == 0 ) && ( c == '0' ) )
    state = 1;
  else if( ( state == 0 ) && ( c == '1' ) )
    state = 2;
  else if( ( state == 1 ) && ( c == '0' ) )
    state = 0;
  else if( ( state == 1 ) && ( c == '1' ) )
    state = 3;
  else if( ( state == 2 ) && ( c == '0' ) )
    state = 3;
  else if( ( state == 2 ) && ( c == '1' ) )
    state = 0;
  else if( ( state == 3 ) && ( c == '0' ) )
    state = 2;
  else if( ( state == 3 ) && ( c == '1' ) )
    state = 1;

  if( state == 2 )
    return 1;
  else
    return 0;
}

int L3( char c )
{
  static int state = 0;

  if( ( state == 0 ) && ( c == '0' ) )
    state = 1;
  else if( ( state == 0 ) && ( c == '1' ) )
    return 0;
  else if( ( state == 1 ) && ( c == '0' ) )
    state = 2;
  else if( ( state == 1 ) && ( c == '1' ) )
    state = 2;
  else if( ( state == 2 ) && ( c == '0' ) )
    state = 1;
  else if( ( state == 2 ) && ( c == '1' ) )
    state = 1;

  if( state == 1 )
    return 1;
  else
    return 0;
}
