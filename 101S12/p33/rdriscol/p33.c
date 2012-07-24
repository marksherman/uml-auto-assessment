/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Recursive Factorial    */
/*                                 */
/* Approx Completion Time: 10 min  */
/***********************************/

#include <stdio.h>



int fact( int x );

int main( int argc, char * argv[]){

 
  int x;
  
  printf( "Enter in any digit:");
  scanf( "%d", &x);
  printf( "The factorial of the number %d is: %d\n", x, fact(x));
  return 0;

}

int fact( int x ){
  if( x==0 )
    return 1;
  if ( x==1 )
    return 1;
  
  return x * fact( x - 1 );

}
