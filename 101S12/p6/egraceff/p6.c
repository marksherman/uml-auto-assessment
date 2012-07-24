/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Equal to Zero?                                                  */
/*                                                                           */
/* Approximate completion time: 20                                           */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int x;
  printf("\nPlease enter a number: \n\n");
  /* prompts the user for a value */
  scanf("%d", &x);
  /* takes the data from the user and stores it into variable x */
  if(x==0) {
    printf("\nThe number is equal to zero.\n\n");
    /* if the condition, x = 0, is met, print the string "The number is equal to zero." */
  }
  else {
    printf("\nThe number is not equal to zero.\n\n");
    /* if the condition is not met, print the sting "The number is not equal to zero." */
  }
  return 0 ;
}
