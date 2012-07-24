/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Malloc up Space for a 1-Dimensional Array of n integers      */
/*                                                                       */
/* Approximate completion time: 1 hour                                   */
/*************************************************************************/
#include <stdio.h>
#include <stdlib.h>
int main( int argc, char *argv[] ) {

  int x, i, sum = 0;
  int *n;

  printf("Please enter a positive integer number:\n");
  scanf("%d", &x);

  n = (int*) malloc (sizeof(int)*x);

  for(i = 0; i < x ; i++){
    printf("Please enter a integer number:\n");
  scanf("%d", &n[i]);
  }
  for(i = 0; i < x; i++)
    sum += n[i];
  printf("%d\n", sum);
  return 0;
}
