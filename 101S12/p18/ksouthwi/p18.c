/***********************************************/
/* Programmer: Kevin Southwick                 */
/*                                             */
/* Program 18: Area of a Circle                */
/*                                             */
/* Approximate completion time: 10  minutes    */
/***********************************************/

#include <stdio.h>
#include <math.h>

int main ( int argc , char* argv[] ) {

  float R ;

  printf( "Enter radius of a circle. \n" );

  scanf( "%f" , &R );
  
  R = R * R * M_PI ;

  printf( "Area = %f \n" , R );

  return 0;

}
