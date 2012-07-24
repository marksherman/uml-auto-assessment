/******************************************************/
/* Programmer: Joe LaMarca                            */
/* Program: p17 area of a rectangle                   */
/* Approximate time of completion: 5 min             */
/******************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  float x;
  float y;
  float area;

  printf("What is the height:");
  scanf("%f",&x);

  printf("What is the length:");
  scanf("%f",&y);
  
  area=x*y;
  
  printf("The area of that rectangle would be: %f\n",area);
  
  return 0;
}
