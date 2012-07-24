/*Programmer: Scott Sok*/

/*Program p10: Sum of Twenty*/

/*Approximate completion time: 1 hour*/

#include <stdio.h>

int main()

{

  int x, y, z;

  FILE *testdata10;

  testdata10 = fopen ("testdata10", "r");
  fscanf(testdata10,"%d",&x);
  fscanf(testdata10,"%d",&z);

  for(y=0; y<20; y++){
    z = x + z;

  }
  printf("the sum is: %d\n", z);
  
  fclose(testdata10);
 

    return 0;

  }
