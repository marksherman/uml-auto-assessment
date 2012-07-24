/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Reverse                                                          */
/*                                                                           */
/* Approximate completion time: 15 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int i;
  int array[10];
  printf("Please enter the ten integers that you wish to reverse: \n");
  /* prompts the user to enter the values */
  for(i=0;i<10;i++){
  /* loop allows for each integer to be scanned into the array */
    scanf("%d", &array[i]);
  }
  printf("The reverse of the ten integers is as follows: \n");
  for(i=9;i>=0;i--){
  /* loop allows for each integer to be printed to standard output starting 
     with the last integer */
    printf("%d ", array[i]);
  }
  printf("\n");
  return 0 ;
}
