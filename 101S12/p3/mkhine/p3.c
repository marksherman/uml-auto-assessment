/**********************************************************/
/*Programmer : Min Thet Khine                             */
/*                                                        */
/*Program homework: 2 integer values via scanf function   */
/*                                                        */
/* Approximate time : 15 minutes                          */
/**********************************************************/
#include <stdio.h>

int main (int argc, char *argv[]){
  /*declare three variables */
  int num1, num2, sum;
  /*ask user to enter two integers as input */
  printf ("Please enter two integers : ");
  scanf ("%d%d", &num1, &num2);
  /*calculate the sum of two integers */
  sum = num1 + num2;
  /* output the sum */

  printf ("The sum is %d\n", sum);
  
  return 0;
}
