 /*********************************************************/
/* Helen Chan                                            */
/* Assignment p17.c                                      */
/* Due February 28, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/


#include <stdio.h>

int main(int argc, char* argv[ ])

{

  float height, length;

  printf("\nEnter two integers, separated by a space, that will represent the dimensions of a rectangle.\n");
  scanf("%f%f", &height, &length);

  printf("\nThe area of the rectangle with dimensions that was entered is: %f square units\n", height*length);
  scanf("%f%f", &height, &length);

  return 0;
}
