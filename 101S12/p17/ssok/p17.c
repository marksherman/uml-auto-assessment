/*****************************************/
/*Programmer: Scott Sok                  */
/*                                       */
/*Program 17: Area of a rectangle        */
/*                                       */
/*Appoximate Completion time: 15 minutes */
/*****************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
  float x, y, z;

  printf("please enter both length and height: \n");
  scanf("%f%f", &x, &y);
  z = x * y;
 
  printf("Area:%f\n", z);

  return 0;

}

	
