/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 24: Find the Average            */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int main (int argc, char* argv[])
{
  int x, y, i, d;
  float z = 0;
  x = 0;
  d = 4;
  FILE *testdata24;

  testdata24 = fopen ( "testdata24", "r");

  while ((i=0)!= EOF){
    y = fscanf( testdata24, "%d", &x);
    if( y == EOF){ 
      break;
  }
    z = x + z;
  }
  z = z/d; 
  printf( "%f \n", z);
  fclose (testdata24);

  return 0;
}
