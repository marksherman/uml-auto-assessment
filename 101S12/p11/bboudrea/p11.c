/*******************************************/
/*                                         */
/* Programmer: Brian Boudreau              */
/*                                         */
/* Assignment 2: The scanf Function        */
/*                                         */
/* Estimated time of Completion: # minutes */
/*                                         */
/*******************************************/

#include<stdio.h>
#include<stdlib.h>

int main(){
  int value;
  printf("Enter integer\n");
  scanf("%d",&value);
  printf("%d\n\n***END OF PROGRAM***\n\nAnswer: pool(billiards)\n",abs(value));

  return(0);
}
