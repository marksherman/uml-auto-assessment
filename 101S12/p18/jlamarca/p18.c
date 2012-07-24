/*******************************************/
/* Programmer: Joe LaMarca                 */
/* Program: p18 area of a circle           */
/* Approximate time of completion: 5 min   */
/*******************************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[]){

  float x;
  float area;

  printf("What is the radius:");
  scanf("%f",&x);
  
  area=M_PI*x*x;
  printf("The radius of that circle is:%f\n",area); 
  
  return 0;
}
