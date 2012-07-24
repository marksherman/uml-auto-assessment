/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 5: Bigger than 100?                      */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int main(){

  int x;

  printf("\nEnter a number\n");

  scanf("%d", &x);

  if(x > 100) {
    printf("\nThe number is bigger than 100.\n");
  } else {
    printf("\nThe number is not bigger than 100.\n");
  }

  return 0;
}
  
