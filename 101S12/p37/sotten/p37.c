/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 37: Digit Sum Again            */
/*                                       */
/*Approx. Completion Time: 50 mins       */
/*                                       */
/*****************************************/

#include <stdio.h>
#include <stdlib.h>
int a;
int sum;
int digit_sum( int a );
int main( int argc, char* argv[] ){
  FILE* nums;
  int a;
  nums = fopen( argv[1], "r" );
  while( (fscanf( nums, "%d", &a)) != EOF )
    printf( "%d\n", digit_sum( a ) );
  return 0;
}
int digit_sum( int a ){
  int end; 
  int sum=0;
  while( a>0 ){
    end = a % 10;
    sum = sum + end;
    a = a / 10;
  }            
  while( a>0 ){
    end = a % 10;
    sum = sum + end;
    a = a / 10;
  }
  return sum;
}
