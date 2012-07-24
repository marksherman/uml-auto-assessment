/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Non-recursive Factorial           */
/*                                              */
/*     Time to Completion: 20 mins              */
/*                                              */
/************************************************/

#include<stdio.h>

int factorial( int num );

int main( int argc, char *argv[] ) {
  
  int num;
  
  printf( "Enter the integer you want to take the factorial of:" );
  scanf( "%d", &num);
  
  int factorial( int num ) {
    int fact = 1;
    
    while( num>0 ) {
      fact *= num;
      num--;
    }
    
    return fact;
  }
  
  printf( "The factorial of that number is:%d\n", factorial(num) );
  
  return 0;
  
}
