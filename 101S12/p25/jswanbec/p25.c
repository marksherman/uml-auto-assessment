/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 25: Unfilled Box                  */
/*                                           */
/* Approximate completion time: 12 minutes   */
/*********************************************/

#include <stdio.h>

int main( int argc, char *argv[] )
{
  int W;
  int H;
  int x;
  int y;
  printf( "Enter a value for width: " );
  scanf( "%d",&W );
  printf( "Enter a value for height: " );
  scanf( "%d",&H );
  for( x=0;x<W;x++ )
    {
      printf( "*" );
    }
  printf( "\n" );
  for( x=0;x<( H-2 );x++ )
    {
      printf( "*" );
      for( y=0;y<( W-2 );y++ )
	{
	  printf( " " );
	}
      printf( "*\n" );
    }
  for( x=0;x<W;x++ )
    {
      printf( "*" );
    }
  printf( "\n" );
  return 0;
}
