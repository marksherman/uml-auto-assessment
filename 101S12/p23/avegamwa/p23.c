/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p23: fgetc, toupper            */
/*                                        */
/* Approximate completion time:15 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[])
{

  int x; 
  FILE *testdata23;

  testdata23 = fopen( "testdata23", "r" );

  while( (x = fgetc(testdata23)) != EOF ){
    putchar( toupper(x) );

    }
  fclose( testdata23 );

  return 0;

}
