/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p21: scanf                     */
/*                                        */
/* Approximate completion time:15 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[])
{

  int x = 0;
  int retVal, true;
  FILE *testdata21;

  testdata21 = fopen( "testdata21", "r" );

  while( true ){ 
    retVal = fscanf( testdata21, "%d", &x );
    if( retVal == EOF ){
      break;
    }
    else{
	printf( "%d ", x );
      
    }
  }

  printf( "\n" );
  fclose( testdata21 );

  return 0;

}
