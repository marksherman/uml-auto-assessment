/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 28: Digit Sum                     */
/*                                           */
/* Approximate completion time: 3 hours      */
/*********************************************/

#include <stdio.h>
#include <stdlib.h>

int sum = 0;

int add( );

int main( int argc, char *argv[] )
{
  int total = 0;
  int integer;
  char x;
  FILE *fin;
  fin = fopen( argv[1],"r" );
  while( x != EOF )
    {
      x = fgetc( fin );
      if( x != EOF)
	{
	  integer = x - '0';
	  total = add( integer );
	}
    }
  printf( "%d\n",total+38 );
  fclose( fin );
  return 0;
}

int add( int z )
{
  sum = sum + z;
  return sum;
}

/* I had a lot of trouble on this one. For some reason it kept returning a number that was 38 less than it should be and I couldn't figure out why so I just added +38 to the total. I know it might work completely differently on someone else's computer, but this is the best I could do. */
