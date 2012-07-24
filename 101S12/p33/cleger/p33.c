/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Recursive Factorial               */
/*                                              */
/*     Time to Completion: 15 mins              */
/*                                              */
/************************************************/

#include<stdio.h>

int factorial( int num );

int main( int argc, char *argv[] ) {
  
  int num;
  
  printf( "Enter the integer you want to take the factorial of:" );
  scanf( "%d", &num);
  
  int factorial( int num ) {
    if(num == 0)
      return 1;
    else
      return num*factorial(num-1);
  }
  
  printf( "The factorial of that number is:%d\n", factorial(num) );
  
  return 0;
  
}
