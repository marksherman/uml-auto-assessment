/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: The abs Function                                                 */
/*                                                                           */
/* Approximate completion time: 30                                           */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
int main( int argc, char *argv[] )
{
  int x;
  printf("Please enter a number: ");
  /*ask the use to input a value*/
  scanf("%d", &x);
  /*store the value into the variable x*/
  printf("%d\n", abs(x));
  /*print the absolute value of the variable x*/
  return 0;
}
