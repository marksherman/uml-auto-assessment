/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 12 sqrt()                                                            */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>
#include <math.h>

int main(){
  float nin;
  printf("Please enter a number ");
  scanf("%e", &nin);
  nin = sqrt(nin);
  printf("Square root is: %e\n", nin);
  return 0;
}
