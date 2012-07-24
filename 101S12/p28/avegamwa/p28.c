/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p28: Digit Sum                 */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int compute_sum();

int main(int argc, char* argv[])
{

  FILE *testdata28;
  int sum;
  int num_from_file;

  
  testdata28 = fopen( argv[1], "r" );

  while( fscanf( testdata28, "%d", &num_from_file) != EOF){
      sum = compute_sum(num_from_file);
      printf("%d", sum );
  }
  printf( "\n" );
  return 0;
}

int compute_sum(int input){
  
  int digit = 0;
  int sum = 0;

  while( input > 0 ){
    digit = input % 10;
    input /= 10;
    sum = sum + digit;
  }

  return sum;
}
