/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 33: Recursive Factorial             */
/*                                            */
/*Approximate completeion time: 10 minutes    */
/**********************************************/

#include <stdio.h>

int factorial( int num );

int main(int argc, char* argv[]){  

  int x;

  printf( "enter an integer number\n" );

  scanf( "%d", &x );

  x = factorial( x );

  printf( "The factorial of the nmber is: %d\n", x );

  return 0;
}

int factorial( int num ){

  if( num == 1 || num == 0 ){

    return 1;

  }else{

    return num * factorial( num - 1 );
  }
}
