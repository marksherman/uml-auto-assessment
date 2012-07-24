/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Digit Sum              */
/*                                 */
/* Approx Completion Time: 30 min  */
/***********************************/

#include <stdio.h>
#include <math.h>


int digitsum( int n );

int main( int argc, char * argv[]){
  int n;
  FILE * fin;
  fin = fopen( argv[1], "r");
  
  while(fscanf( fin, "%d", &n) != EOF){
    printf( "The sum of %d is %d. \n",n, digitsum(n));
    if( NULL == fin){
      printf( "Failure to open file:\n");
      return 1;
    }
  }
  fclose( fin );
  return 0;
}



/*  
I got help from: 
http://cboard.cprogramming.com/c-programming/107642-digit-sum.html*/ 
int digitsum( int n ){
  int digit,sum = 0;
  while( n > 0 ){
    digit = n % 10;
    n = n / 10;
    sum = sum + digit;
  }
  return sum;
}
