/********************************************/
/*                                          */
/* Programmer: Brian Boudreau               */
/*                                          */
/* Assignment 6: Equal to zero?             */
/*                                          */
/* Estimated time of Completion: 15 minutes */
/*                                          */
/********************************************/

#include<stdio.h>

int main(){
  int x=0;
  printf("Please enter an integer\n");
  scanf("%d",&x);
  if(x==0)
    printf("The number is equal to zero\n");
  else
    printf("The number is not equal to zero\n");


  return(0);
}
