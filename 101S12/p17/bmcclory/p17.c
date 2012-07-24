/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #17: Area of a Rectangle                      */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])

{

  float L, H;
  double A;

  printf("Type the length of the rectangle: ");
  scanf("%f", &L); /* "control char", "control char", "&VAR1", "&VAR2" gave me an writing into ocnstant object message */

  printf("Type the height of the rectangle: ");
  scanf("%f", &H);

  A = L * H;

  printf("%f\n", A);

  return 0;
}
