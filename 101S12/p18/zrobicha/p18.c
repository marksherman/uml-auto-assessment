/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Area of a Circle                                             */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <math.h> 

int main( int argc , char* argv[]) {
  float area , rad ;
  printf( "Enter the radius of the circle :\n" ) ;
  scanf( "%f" , &rad ) ;
  area = rad * rad * M_PI ; 
  printf( "The area of the circle is %f\n" , area ) ;
  return 0 ;
}
