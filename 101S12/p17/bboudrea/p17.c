/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 17: Area of a Rectangle        */
/*                                           */
/* Estimated time of Completion: 20  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
  double l=0;
  double w=0;
  printf("Please enter length:\n");
  scanf("%lf", &l);
  printf("Please enter width:\n");
  scanf("%lf", &w);
  printf("The area of the rectangle is %lf square units\n",l*w);

  return(0);
}
