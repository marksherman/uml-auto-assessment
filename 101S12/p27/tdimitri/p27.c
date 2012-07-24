/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 27: Reverse                           */
/* Approximate completion time: 15 mins          */
/*************************************************/

#include<stdio.h>

int main(int argc, char* argv[]) {
  int i[10], x;
  x = 0;
  printf("Enter 10 numbers:\n");
  while( x <= 9){
    scanf("%d", &i[x]);
    x++;
  }
  for( x = 9; x >= 0; x--)
    printf("%d ", i[x]);
  putchar('\n');

  return 0;
}
