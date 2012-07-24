/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #12: Using the sqrt Function                  */
/*                                                       */
/* Approximate Completion Time: At least 90 min          */
/*********************************************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[])

{
  float i;
  int j;

  printf("Type a positive number: ");
  scanf("%f", &i);

  i = sqrt(i);

  j = (int)i;

  printf("%d\n", j);
  
  return 0;
}
