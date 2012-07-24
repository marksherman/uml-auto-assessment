/****************************************************************************/
/* Jennifer Ly                                                              */
/* p13.c                                                                     */
/* Computing1                                                               */
/****************************************************************************/

#include<stdio.h>
#include<stdlib.h>
#include<math.h>

int main(int argc, char*argv[ ])
{

  float h;

  h = atof(argv[1]);
  h = sin(h);

  printf("The trigonometric sine value of the number in radian is: %f.\n", h);

  return 0;
}
