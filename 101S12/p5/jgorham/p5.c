/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 5: Greater than 100?                                                 */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>

int main(){
  int nin;
  printf("Please enter a number. ");
  scanf("%d",&nin);
  if(nin > 100)
    printf("The number is bigger than 100.\n");
  else
    printf("The number is not bigger than 100.\n");
  return 0;
}
