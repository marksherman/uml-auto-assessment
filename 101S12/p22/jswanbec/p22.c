/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 22: Sum of a Bunch                */
/*                                           */
/* Approximate completion time: 3 minutes    */
/*********************************************/

#include <stdio.h>

int main( int argc, char *argv[] )
{
  int x;
  int y;
  y = 0;
  FILE *fin;
  fin = fopen( "testdata22","r" );
  while( fscanf( fin,"%d",&x ) != EOF)
    {
      y = x + y;
    }
  printf( "%d\n",y );
  fclose( fin );
  return 0;
}
