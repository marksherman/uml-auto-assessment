/********************************/
/* Author: James DeFilippo      */
/* Title : The sqrt function    */
/* Approximate Time: 5 minutes  */
/********************************/

#include <stdio.h>
#include <math.h> /* sqrt(x) is contained here */ 

int main ( int argc, char *argv[] )
{
  double x; /* initialize a variable for scanf toread data into */ 
  printf( "Please enter a floating point number.\n" ); /* prompt the user for meaningful input */
  scanf( "%lf", &x ); 
  printf( "The square root of the number you entered is %lf\n", sqrt(x) ); 
  /* integrates a math function as an argument to printf */ 
  return 0;
}






