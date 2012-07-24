/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Reverse                                                      */
/*                                                                       */
/* Approximate completion time: 10 minutes                               */
/*************************************************************************/
#include <stdio.h>

int main (int argc , char* argv []) {
  int i;
  int B[10];
 
  printf("Please enter 10 integers\n");
 
  for( i = 0; i < 10; i++)scanf("%d", & B[i]);
  for( i = 9; i >=0; i--)
    printf("%d  ",B[i]);

  return 0;
}
