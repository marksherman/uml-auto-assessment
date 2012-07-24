/**************************************************/
/* Programmer: Harrison Kelly                     */
/*                                                */
/* Program Name: p6 Equal to Zero?                */
/*                                                */
/* Approximate completion time: 5 minutes         */
/**************************************************/

#include <stdio.h>

int main(){

  int x;

  printf("\nEnter a Number:\n");
  scanf("%d", &x);

  if( x==0 ){
    printf("\nThe number is equal to zero.\n");
  }

  else{
    printf("\nThe number is not equal to zero.\n");
  }
  return 0;
}
