/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #7: Positive, Negative, or Zero?              */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

int main()
{
  int value;

  printf("Type an integer: ");
  scanf("%d", &value);

  if (value == 0){
    printf("The number you entered is zero\n");
  }
  else if (value < 0){
    printf("The number you entered is negative\n");
  }
  else{
    printf("The number you entered is positive\n");
  }

  return 0;
}
