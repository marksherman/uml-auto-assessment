/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/* Program 6: Equal to Zero?      */
/*                                */
/*Approx. Completion Time: 25mins */
/*                                */
/**********************************/

#include <stdio.h>

int main (){

  int y;

  printf("Select keyboard integer\n");

  scanf ("%d",&y);

  if(y==0){
    printf("The number is equal to zero\n");
}
  else if(y<0){
    printf("The number is negative\n");
}
  else 
    printf("The number is positive\n");
  return 0;
}
