/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Persistence of a Number                                      */
/*                                                                       */
/* Approximate completion time: 45 minutes                               */
/*************************************************************************/
#include <stdio.h>

extern int persistence( int n ) ;
int main( int argc, char *argv[] ) {
  
  int n, x;
  printf("Please enter a positive integer until EOF:\n");
  x = scanf("%d", &n) ;
  while( x != EOF) {
    n = persistence(n);
    printf("The persistence is %d\n", n);
    x=scanf("%d", &n) ;
  }
  return 0;
}

int persistence( int n ) {
  int count = 0,  product = 1;
  
  while( n > 0) { 
    product *= n % 10;
    n /= 10 ;
    
    if (n == 0){ 
      count ++;
      if( product > 9 ){
	n = product;
	product = 1;
      }
    }
  }
  return count;
}
