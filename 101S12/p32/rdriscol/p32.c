/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Non Recursive Factorial*/
/*                                 */
/* Approx Completion Time: 1hr     */
/***********************************/


#include <stdio.h>



int fact( int n);

int main( int argc, char *argv[] ){
 
  int n;


  printf( " Enter a single digit: " );

  scanf( "%d", &n );
      
  printf( "The factorial of that number you selected %d is:%d\n", n, fact(n) );

  return 0;

}

int fact( int n ){

  int i;
  int digit = 1;
  
  if( n==0 )
    return 1;


  for( i = n; i > 1; i--){
    digit = digit * i;
  }

  return digit;
}
