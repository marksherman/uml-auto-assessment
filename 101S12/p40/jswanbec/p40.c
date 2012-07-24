/**************************************************************/
/* Programmer: Jimmy Swanbeck                                 */
/*                                                            */
/* Program 40: Multiple Deterministic Finite State Machines   */
/*                                                            */
/* Approximate completion time: 190 minutes                   */
/**************************************************************/

#include <stdio.h>

void language1( char );

int state1 = 0 , lang1accept = 0;

void language2( char );

int state2 = 0 , lang2accept = 0;

void language3( char );

int state3 = 0 , lang3accept = 0;

int main( int argc , char *argv[] )
{
  char input = '0';
  printf( "Input a string of symbols: " ); 
  while(( input == '1' ) || ( input == '0' ))
    {
      input = getchar( );
      if(( input == '1' ) || ( input == '0' ))
	{
	  language1( input );
	  language2( input );
	  language3( input );
	}
    }
  if( lang1accept == 1 )
    printf( "Language 1 accepts.\n" );
  else
    printf( "Language 1 rejects.\n" );
  if( lang2accept == 1 )
    printf( "Language 2 accepts.\n" );
  else
    printf( "Language 2 rejects.\n" );
  if( lang3accept == 1 )
    printf( "Language 3 accepts.\n" );
  else
    printf( "Language 3 rejects.\n" );
  return 0;
}

void language1( char input )
{
  if( state1 == 0 )
    {
      if( input == '0' )
	state1 = 1;
    }
  else if( state1 == 1 )
    {
      if( input == '0' )
	state1 = 1;
      else
	state1 = 2;
    }
  else if( state1 == 2 )
    {
      if( input == '0' )
	state1 = 3;
      else
	state1 = 1;
    }
  if( state1 == 3 )
    lang1accept = 1;
  else
    lang1accept = 0;
}

void language2( char input )
{
  if( state2 == 0 )
    {
      if( input == '0' )
	state2 = 1;
      else
	state2 = 2;
    }
  else if( state2 == 1 )
    {
      if( input == '0' )
	state2 = 0;
      else
	state2 = 3;
    }
  else if( state2 == 2 )
    {
      if( input == '0' )
	state2 = 3;
      else
	state2 = 0;
    }
  else if( state2 == 3 )
    {
      if( input == '0' )
	state2 = 2;
      else
	state2 = 1;
    }
  if( state2 == 2 )
    lang2accept = 1;
  else
    lang2accept = 0;
}

void language3( char input )
{
  if( state3 == 0 )
    {
      if( input == '0' )
	state3 = 1;
      else
	state3 = -1;
    }
  else if( state3 == 1 )
    {
      if(( input == '0' ) || ( input == '1' ))
	{
	state3 = 2;
	}
    }
  else if( state3 == 2 )
    {
      if(( input == '0' ) || ( input == '1' ))
	state3 = 1;
    }
  if( state3 == 1 )
    lang3accept = 1;
  else
    lang3accept = 0;
}
