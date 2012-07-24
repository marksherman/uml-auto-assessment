/*******************************************/
/* Programmer: Thearisatya Thok            */
/*                                         */
/* Program 12 : Square Root Function       */
/*                                         */
/* Approximate completion time:120 minutes */
/*******************************************/

#include <stdio.h>
#include <math.h>
int main()
{
  float num, n;
  printf ("Enter a number: ");
  scanf ("%f", &num);    
  n = sqrt(num);
  printf("%f\n", n);

  return 0;  
}
