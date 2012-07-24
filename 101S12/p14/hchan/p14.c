/*********************************************************/
/* Helen Chan                                            */
/* Assignment p14.c                                      */
/* Due February 21, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/

#include<stdio.h>
#include<stdlib.h>
#include<math.h>

int main(int argc, char*argv[ ])
{

  float h;

  h = atof(argv[1]);
  h = sin(h);

  printf("The trigonometric sine value of the number (in radians) is %f.\n", h); 

  return 0;
}
