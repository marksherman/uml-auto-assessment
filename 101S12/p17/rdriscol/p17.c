/************************************/
/* Programmer: Rachel Driscoll      */
/*                                  */
/* Title: Area of a Rectangle       */
/*                                  */
/* Approx Completion Time: 30 min   */
/*                                  */
/************************************/

#include <stdio.h>

 
int main (int argc, char *argv[]){
  
  float l,h,a;
  printf( "Enter two numbers here with a space between them:" );
  scanf( "%f %f", &l,&h);
  a = l*h;
printf( "The Area of the Rectangle is %f \n", a);
  return 0;
}
