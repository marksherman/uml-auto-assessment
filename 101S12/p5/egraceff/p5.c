/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Is this number bigger than 100?                                  */
/*                                                                           */
/* Approximate completion time: 15                                           */
/*****************************************************************************/

#include <stdio.h>
int main (){
  int x;
  printf("\nPlease enter a number: \n\n");
  scanf("%d", &x);
  if (x>100){
    printf("\nThe number is bigger than 100. \n\n");
  }
  else{
    printf("\nThe number is not bigger than 100. \n\n");
  }
  return 0;
}
