/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #3: Sum of two values                         */
/*                                                       */
/* Approximate Completion Time: 30 Minutes               */
/*********************************************************/

#include <stdio.h>

int main()
{
  int v1;
  int v2; 
  int sum;

  printf("Type two integers: ");
  scanf("%d", &v1);
  scanf("%d", &v2);
 
  sum = v1 + v2;
  
  printf("%d\n", sum);

  return 0;
}
