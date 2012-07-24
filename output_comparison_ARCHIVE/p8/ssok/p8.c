/*Programmer: Scott Sok*/

/*Program p8: One Horizontel Line of Asterisks*/

/*Appoximate completion time: 1 hour*/

#include <stdio.h>

int main(){

  int x, y;

  FILE *testdata8;

  testdata8 = fopen("testdata8", "r");
  
  fscanf(testdata8, "%d", &x);

  for(y = 0;  y < x ;   y++) { 
  
  printf("*");
  }
  printf("\n");
  
  fclose(testdata8);
  
  return 0;

}


  
