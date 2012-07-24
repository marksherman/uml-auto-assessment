/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 26: One Dimensional Array         */
/*                                           */
/* Approximate completion time: 10 minutes   */
/*********************************************/

#include <stdio.h>

int main( int argc, char *argv[] )
{
  int h;
  int i;
  int j[15];
  FILE *fin;
  fin = fopen( "testdata26","r" );
  for( i=0;i<15;i++ )
    {
      fscanf( fin,"%d",&h );
      j[i] = h;
    }
  for( i=14;i>=0;i-- )
    {
      printf( "%d",j[i] );
      printf( "\n" );
    }
  fclose( fin );
  return 0;
}
