/********************************/
/* Author: James DeFilippo      */
/* Title : Sum of Two Values    */
/* Approximate Time: 10 minutes */
/********************************/

#include <stdio.h>
int main ( int argc, char *argv[] )
{
  int x, y, sum;
  printf( "Please enter two values.\n" ); /* user-friendly prompt */ 
  scanf( "%d %d", &x, &y ); /* this call for scanf allows for two inputs at one time */ 
  sum = x + y; /* determine sum using inputed values */ 
  printf( "The sum of the values you just entered is %d.\n", sum ); /* display the value of sum */ 
  return 0; 
}
