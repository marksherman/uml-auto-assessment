/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Digit Sum                                                    */
/*                                                                       */
/* Approximate completion time: 20 minutes                               */
/*************************************************************************/
#include <stdio.h>
#include <stdlib.h>
extern int digitsum(int n);

int main (int argc , char *argv[]) {
  
  int n , sum ;
  
  n = atoi( argv[1] );
  sum = digitsum( n );
  printf("\nThe sum is %d.\n",sum );
  
  return 0;
}

int digitsum( int n){
  
  int sum = 0;
  while( n > 0) {
    sum += n %10;
    n = n/ 10;
  }    
  return sum;
  
  
}
