/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Digit Sum                                                    */
/*                                                                           */
/* Approximate Completion Time : 40 minutes                                  */
/*****************************************************************************/


#include <stdio.h>
#include <stdlib.h> 


int sum( int x ) ;

int main( int argc , char* argv[] ) {

  int x ;
  FILE* fin ;
    
  fin = fopen( argv[1] , "r" ) ;
  fscanf( fin , "%d" , &x ) ;
  x = sum( x ) ;
  printf( "The sum of the digits is : %d\n" , x ) ;
  fclose(fin) ;
  return 0 ;
}

int sum( int x ) {

  int sum = 0 , i ;

  while( x != 0 ) {
    i = x % 10 ;
    x = x / 10 ;
    sum += i ;
  }
  return sum ;
}
