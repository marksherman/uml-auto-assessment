/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 17: Area of a Rectangle                                              */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  float height = 0;
  float length = 0;
  float area = 0;
  printf("Enter height: ");
  scanf("%f", &height);
  printf("Enter length: ");
  scanf("%f", &length);
  area = height * length;                                                         /*Could have condensed this into the print but perferred the clarity*/
  printf("The rectangle's area is %f.\n", area);
  return 0;
}
