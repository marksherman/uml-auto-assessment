/*Programmer: Scott Sok*/

/*Program p9: Using a FOR loop*/

/*Approximate completion time: 15 minutes*/

#include <stdio.h>

int main(){

  int x, a, b, c, d, e;

  FILE *testdata9;

  testdata9 = fopen("testdata9", "r");

  fscanf(testdata9, "%d%d%d%d%d", &a, &b, &c, &d, &e);

  for( x = 0; x < 1; x++){   
    
  printf("%d\n%d\n%d\n%d\n%d\n", a, b, c, d, e);
 
 }

  fclose(testdata9);

  return 0;

    }
    
