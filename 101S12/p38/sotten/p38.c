/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 38: Recursive Digit Sum        */
/*                                       */
/*Approx. Completion Time: 50 mins       */
/*                                       */
/*****************************************/

#include <stdio.h>
#include <stdlib.h>
int a, sum;
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
  end = a % 10;
  a = a / 10;
  if(a==0){
    return end;
  }
  else{
    return digit_sum((end + a));
    }
}

/* not 100% sure why it won't run...it works oddly enough for smaller integers less than 3 digits in length....*/ 
