/*******************************************************/
/* Programmer: Jimmy Swanbeck                          */
/*                                                     */
/* Program 38: Recursive Digit Sum                     */
/*                                                     */
/* Approximate completion time: 23 minutes             */
/*******************************************************/

#include <stdio.h>

int sum( );

int main( int argc , char *argv[] )
{
  int x , y = 0;
  FILE *fin;
  fin = fopen( argv[1] , "r" );
  while( y != EOF )
    {
    y = fscanf( fin , "%d" , &x );
    if( y != EOF )
      printf( "Digit Sum of %d: %d\n" , x , sum( x ) );
    }
  fclose ( fin );
  return 0;
}

int sum( int x )
{
  int y = x%10;
  if( x > 0 )
    return ( y + sum( x/10 ));
  else
    return 0;
}
