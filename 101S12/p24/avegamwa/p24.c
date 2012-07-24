/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p24: average                   */
/*                                        */
/* Approximate completion time:15 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
int main(int argc, char* argv[])
{

  int x, i, retVal;
  float sum = 0;
  FILE *testdata24;

  testdata24 = fopen( "testdata24", "r" );

  while( (i=0) !=EOF ){
    retVal = fscanf( testdata24, "%d", &x );
    if( retVal == EOF ){
      break;
    }
    sum = sum + x;
  }

  sum = sum / 4;
  printf( "%f", sum );
  printf( "\n" );
  fclose( testdata24 );

  return 0;

}
