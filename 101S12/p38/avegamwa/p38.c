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

int compute_sum(int digits);

int main(int argc, char* argv[])
{

  FILE *testdata38;
  int sum;
  int num_from_file;


  testdata38 = fopen( argv[1], "r" );

  while( fscanf( testdata38, "%d", &num_from_file) != EOF){
    sum = compute_sum(num_from_file);
    printf("%d ", sum );
  }
  printf( "\n" );
  return 0;
}

int compute_sum(int digits){

  if( digits < 10 )
    return digits;
  else
    return (digits % 10 + compute_sum(digits / 10));
}

