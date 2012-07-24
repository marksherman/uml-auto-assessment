/*********************************************/
/* Programmer: Jimmy Swanbeck                */
/*                                           */
/* Program 21: scanf Returns What?           */
/*                                           */
/* Approximate completion time: 10 minutes   */
/*********************************************/

#include <stdio.h>

int main( int argc, char *argv[] )
{
  int x;
  FILE *fin;
  fin = fopen( "testdata21","r" );
  while( fscanf( fin,"%d",&x ) != EOF)
    {
      printf( "%d\n",x );
    }
  fclose( fin );
  return 0;
}
