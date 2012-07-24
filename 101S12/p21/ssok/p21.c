/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 21: Scanf returns what          */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
  int x, y, z;
  x = 0;
  
  FILE *testdata21;

  testdata21 = fopen( "testdata21", "r");

  while((y = 0) != EOF){
    z = fscanf( testdata21, "%d", &x);
    if( z == EOF){
      break;
    }
    else{
      printf("%d ", x);
    }
  }
  printf("\n");
  fclose( testdata21 );

  return 0;
}

