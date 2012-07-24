/*****************************************/
/* Programmer: Joanna Sutton             */
/*                                       */
/* Assignment: Reverse                   */
/*                                       */
/* Approximate Completion Time:15 minutes*/
/*****************************************/

#include <stdio.h>

int main (int argc, char*argv[]){
  int i;
  int j;
  int numbers[10];

  printf("Please enter ten integers.\n");

  for(i=0;i<10;i++)
    scanf("%d", &numbers[i]);
  
  for(j=9;j>-1;j--)
    printf("%d ", numbers[j]);

  putchar('\n');

  return 0;

}
