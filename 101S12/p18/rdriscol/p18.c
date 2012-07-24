/************************************/
/* Programmer: Rachel Driscoll      */
/*                                  */
/* Title: Area of a Circle          */
/*                                  */
/* Approx Completion Time: 30 min   */
/*                                  */
/************************************/

#include <stdio.h>
#include <math.h>

int main(int argc, char *argv[]){

  float r,c;
 
  printf( " Enter a number here: ");
    scanf( "%f",&r);
    c = M_PI*r*r;
     printf( "The Area of a Circle is %f \n", c);
  return 0;
}
