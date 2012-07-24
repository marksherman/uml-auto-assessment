/**********************************************/
/* Programmer: Kevin Southwick                */
/*                                            */
/* Program 39: Recursive Persistence          */
/*                                            */
/* Approximate completion time: 30  minutes   */
/**********************************************/

#include <stdio.h>

void getnum( void );
int persistence( int x );
int product( int x );

int main( int argc , char* argv[] ) {

  getnum(); 
/* the work done in getnum needs to be repeated */
/* so the work gets its own recursive function  */

  return 0;

}

void getnum(){

  int x;

  printf( "Input a positive integer , quit with C-d once or twice \n");

  if( ( scanf( "%d" , &x ) ) == EOF )
    /*******************************************************************/
    /* the quit condition, scans in the number, then determines if the */
    /* program needs to start returning , and then quit                */
    /* so this function is going to be called one more time than the   */
    /* number of inputted numbers, then cascade returns to other calls */
    /* of itself, and finally to main                                  */
    /*******************************************************************/
    return;

  else{
  
    printf( "The persistence of %d is: %d \n" , x , persistence( x ) );
    /* The call to the persistence function, and print the answer   */

    getnum(); /*the recursive call, so we can get as many numbers as we want*/

    return;

  }
}

int persistence( int x ){

  if ( x < 10 )
    /* the base case, the number is one digit long */
    return 0;

  else{

    x = product( x ); /* x becomes the product of its digits */
    
    return( 1 + persistence( x ) ); 
    /********************************************************************/
    /* the recursive call and counter                                   */
    /* counts the number of times persistence had to be called to get x */
    /* down to only one digit                                           */
    /********************************************************************/

  }

}

int product( int x ){

  if( x < 10 )
    /* the base case, x is one digit */
    return x;

  else
 
    return( ( x % 10 ) * product( x / 10 ) );
  /* the recursive call, gets the last digit of x, then multiplies that  */
  /* by the product of the remaining digits, then returns the result     */

}
