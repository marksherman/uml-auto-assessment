/****************************************/
/* Name: Kyle Champney                  */
/*                                      */
/* Program: p17                         */
/*                                      */
/* Estimated Completion Time: 5 mins    */
/****************************************/

#include <stdio.h>

int main(){

  printf("Please input two floating point numbers that represent the length");
  printf(" and width of a rectangle: ");

  float L;
  float W;
  scanf("%f %f", &L, &W);

  float area = L * W;

  printf("The area of the rectangle is %f\n", area);

  return 0;
}
