/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Postive, Negative, or Zero                                       */
/*                                                                           */
/* Approximate completion time: 10                                           */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int x;
  printf("\nPlease enter a number: \n\n");
  /* prompts the user for a value */
  scanf("%d", &x);
  /* store the value that the user inputs into the variable x */
  if(x>0){
    printf("\nThen number is positive.\n\n");
    /* if x > 0, print the string "The number is positive" */
  }
  else
  /* if the condition, x>0, is not met, run the following */
    if(x<0){
      printf("\nThe number is negative.\n\n");
      /* if the condition, x<0, is met, print the string "The number is negative" */
    }
    else {
      printf("\nThe number is zero.\n\n");
      /* if the condition is not met, print the string "The number is zero." */
    }
  return 0 ;
}
