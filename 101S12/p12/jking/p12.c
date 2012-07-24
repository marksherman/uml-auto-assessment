/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 12: The sqrt Function                                  */
/* Approx Completion Time: 20 Mintues                             */
/******************************************************************/

#include<stdio.h>
#include<math.h>

int main( int argc, char* argv [] ){
 
  float x;

  printf( "Enter a floating point number:\n" );
  scanf( "%f", &x );
  if( x < 0 ){
    printf( "Cannot take square root of a negative number!\n" );
}  
  else {
    x = sqrt( x );
    printf( "The Square Root of the float you entered is:\n%f\n", x );
}
  return 0;
}

