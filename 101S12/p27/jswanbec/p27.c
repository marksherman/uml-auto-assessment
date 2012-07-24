/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 27: Reverse                       */
/*                                           */
/* Approximate completion time: 9 minutes    */
/*********************************************/

#include <stdio.h>

int main( int argc, char *argv[] )
{
  int h;
  int i;
  int j[10];
  for( i=0;i<10;i++ )
    {
      printf( "Enter a value: " );
      scanf( "%d",&h );
      j[i] = h;
    }
  for( i=9;i>=0;i-- )
    {
      h = j[i];
      printf( "%d\n",h );
    }
  return 0;
}
