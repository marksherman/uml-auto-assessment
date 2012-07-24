/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 24: Find the Average              */
/*                                           */
/* Approximate completion time: 8 minutes    */
/*********************************************/

#include <stdio.h>

int main( int argc, char *argv[] )
{
  int x;
  float y = 0;
  float z;
  FILE *fin;
  fin = fopen( "testdata24","r" );
  while( fscanf( fin,"%d",&x ) != EOF)
    {
      y = y + x;
    }
  z = y / 4;
  printf( "The average of the four values is: %f\n",z );
  fclose( fin );
  return 0;
}
