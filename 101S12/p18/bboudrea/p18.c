/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 18: Area of a Cicle            */
/*                                           */
/* Estimated time of Completion: 20  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>
#include<math.h>

int main(int argc, char* argv[]){
  double r=0;
  double pi=M_PI;
  printf("Please enter radius:\n");
  scanf("%lf", &r);
  printf("The area of the cicle is %lf square units\n",r*r*pi);

  return(0);
}
