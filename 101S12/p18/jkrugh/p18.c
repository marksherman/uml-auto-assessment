/***************************************************************/
/* Programmer: Jeremy Krugh                                    */
/*                                                             */
/* Program 18: Area of a Circle                                */
/*                                                             */
/* Approximate time of completion: 15 minutes                  */
/***************************************************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[]){

  float r;
  float a;

  printf("Enter the radius of the Circle: ");
  scanf("%f",&r);

  a = (M_PI*r*r);

  printf("The area of the Circle is: %f\n",a);

  return 0;
}
