
/* Programmer: Rachel Driscoll    */
/*                                */
/* Title: The abs Function        */
/*                                */
/* Approx Completion Time: 30 min */
/*                                */

#include <stdlib.h>
#include <stdio.h>

int main(){

  int x,k;

  printf(" Enter any number here: ");
  scanf("%d",&x);
  k = abs(x);
  printf(" The absolute value of this number is %d\n",k);
  return 0;
}
