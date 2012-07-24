/*****************************************************/
/* Programmer: Harrison Kelly                        */
/*                                                   */
/* Program Name: p8 One Horizontal Line of Asterisks */
/*                                                   */
/* Aproximate completion time: 10 minutes            */
/*****************************************************/

#include <stdio.h>

int main(){

  FILE* fin;
  int x, y;
  
  fin = fopen("testdata8", "r");
  fscanf(fin, "%d", &x);
  fclose(fin);

  for( y = 0; y <= x; y++ ){
    printf("*");
  }

  printf("\n");

  return 0;
}
