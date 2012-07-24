/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #6: Equal To Zero?                            */
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
    printf("The number you entered is equal to zero\n");
  }
  else{
    printf("The number you entered is not equal to zero\n");
  }

  return 0;
}
