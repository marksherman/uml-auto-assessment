/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p16                      */
/*                                   */
/* Estimated Completion Time: 15 min */
/*************************************/

#include <stdio.h>

int main(){

  int x;
  char c = getchar();

  while(c != EOF){
    c = getchar();
    x++;
  }
  
  printf("\n%d\n", x);

  return 0;
}
