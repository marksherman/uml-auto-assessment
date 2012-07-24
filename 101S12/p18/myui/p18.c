/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Area of a Circle                 */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>
#include<math.h>

int main( int argc, char *argv[] ) {
  
  float radius, area;
  
  printf( "Enter a radius: ");
  scanf( "%f", &radius );
  
  area = M_PI * radius * radius;

  printf( "The area is %f.\n", area );  

  return 0;
}
