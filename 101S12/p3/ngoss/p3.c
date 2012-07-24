/***********************************************/
/* Programmer: Nathan Goss                     */
/*                                             */
/* Program 3: Sum of Two Values                */
/*                                             */
/* Approximate completion time: 2 minutes      */
/***********************************************/


#include <stdio.h>

int main()
{

  int sum,val1,val2;

  printf("Input two integers: "); 

  scanf("%d %d", &val1, &val2);

  sum = val1 + val2;

  printf("The sum of %d and %d is %d\n", val1, val2, sum);

}
