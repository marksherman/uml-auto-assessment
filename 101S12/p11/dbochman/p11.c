/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 11:   The abs Function                       */
/* Time:         3 minutes                              */
/********************************************************/


#include <stdio.h>

int main () {

  int a, n;

  printf("Please enter an integer \n");

  scanf("%d",&a);

  n=abs(a);

  printf("The absolute value of the integer you entered is %d\n",n);  

  return 0 ;

}
