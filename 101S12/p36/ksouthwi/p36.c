/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 36: Persistence of a Number        */
/*                                            */
/* Approximate completion time: 60  minutes   */
/**********************************************/

#include <stdio.h>

int persistence( int x );

int main( int argc , char* argv[] ) {

  int x ;

  printf( "Input positive integers , stop with C-d once or twice \n");

  while( (scanf( "%d" , &x )) != EOF )

    printf( "The persistence of %d is: %d \n" , x , persistence( x ) );

  return 0;

}

int persistence( int x ){

  int  j , product = 1 ;

  for( j = 0 ; x >= 10 ; j++ ){ 
    /* this loop counts the persistence number */

    while(  x > 0 ){ 
      /* x will be 0 after the last iteration          */
      /* this loop gets the product of the digits of x */

      product = product * ( x % 10 ); 
           /* multiplies the product by the next digit */

      x = ( x - ( x % 10 ) ) / 10 ; 
           /* take off the last digit of x for the next check and iteration */

    }

    x = product ; /* sets x to the product for next iteration of the j loop */

    product = 1; /* resets product for the next iteration of the j loop */

  }

  return j;

}
