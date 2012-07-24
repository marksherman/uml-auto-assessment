/**************************************************/
/*                                                */
/* Programmer: Brian Boudreau                     */
/*                                                */
/* Assignment 3: Sum of two values                */
/*                                                */
/* Estimated time of Completion: 15 minutes       */
/*                                                */
/* Description: This program reads two integer    */
/*  values from the keyboard, adds them together, */
/*  then displays the sum in the form:            */
/*                                                */
/*          term1+term2=sum                       */
/*                                                */
/**************************************************/

#include<stdio.h>

int main(){
  int term1, term2, sum=0;
    printf("Enter two values to be added\n");
  scanf("%d %d",&term1,&term2);
  sum=term1+term2;
  printf("%d+%d=%d\n",term1,term2,sum);

  return(0);
}
