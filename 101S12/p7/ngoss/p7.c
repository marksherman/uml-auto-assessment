/*******************************************/
/* Programmer: Nathan Goss                 */
/*                                         */
/* Program 7: Positive, Negative, or Zero? */
/*                                         */
/* Approximate completion time: 2 minutes  */
/*******************************************/


#include <stdio.h>

int main()
{
  int val;

  printf("Input an integer: ");
  scanf("%d", &val);

  if (val > 0)
    printf("The number is positive.\n");
  else if(val < 0)
    printf("The number is negative.\n");
  else
    printf("The number is zero.\n");

  return 0;

}
