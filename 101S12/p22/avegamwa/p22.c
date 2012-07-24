/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p22: sum                       */
/*                                        */
/* Approximate completion time:15 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[])
{

  int x = 0;
  int  i, retVal;
  int sum = 0;
  FILE *testdata22;

  testdata22 = fopen( "testdata22", "r" );

  while( (i=0) !=EOF ){
    retVal = fscanf( testdata22, "%d", &x );
    if( retVal == EOF ){
      break;
    }
    sum = sum + x;
  }

  printf( "%d ", sum );
  printf( "\n" );
  fclose( testdata22 );

  return 0;

}
