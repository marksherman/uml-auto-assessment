/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #5: Bigger Than 100?                          */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

int main()
{
  int value;

  printf("Type an integer: ");
  scanf("%d", &value);

  if (value < 100){
    printf("The number you entered is less than 100\n");
  }
  else{
    printf("The number you entered is bigger than 100\n");
  }

  return 0;
}
