/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 22: Sum of a bunch              */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
  int x, y, z, i;
  x = 0;
  z = 0;
  FILE *testdata22;

  testdata22 = fopen( "testdata22", "r");
  
  while ( (i = 0) != EOF){
    y = fscanf (testdata22, "%d", &x);
    if ( y == EOF ){
      break;
    }
    z = x + z;
  }
  printf( "%d \n", z);
  fclose( testdata22);

  return 0;
}
