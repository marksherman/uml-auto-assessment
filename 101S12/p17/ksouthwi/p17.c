/***********************************************/
/* Programmer: Kevin Southwick                 */
/*                                             */
/* Program 17: Area of a Rectangle             */
/*                                             */
/* Approximate completion time: 10  minutes    */
/***********************************************/

#include <stdio.h>

int main ( int argc , char* argv[] ) {

  float L, H ;

  printf( "Enter dimension of a rectanle. \n" );

  scanf( "%f %f" , &H , &L );
  
  L = L * H;

  printf( "Area = %f \n" , L );

  return 0;

}
