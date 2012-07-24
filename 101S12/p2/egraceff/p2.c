/*****************************************************************************/
/* Programmer: Erin R. Graceffa                                              */
/*                                                                           */
/* Program: The scanf Funtion                                                */
/*                                                                           */
/* Approximate completion time: 40                                           */
/*****************************************************************************/

#include <stdio.h>
int main (){
  int integer;
  printf("Please enter a number: \n");
  scanf("%d", &integer);
  printf("\nYou entered: %d \n", integer);
  return 0;
}
