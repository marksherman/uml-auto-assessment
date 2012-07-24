/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 28: Digit Sum                   */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int sum();
int main(int argc, char* argv[])
{
  int x;
  int num_from_file;
  FILE *testdata28;

  testdata28 = fopen( argv[1], "r" );

  while( fscanf( testdata28, "%d", &num_from_file) != EOF){
    x = sum(num_from_file);
    printf("%d \n", x );
  }
  return 0;
}

int sum(int input){
  int digit = 0;
  int x = 0;

  while( input > 0 ){
    digit = input % 10;
    input /= 10;
    x = x + digit;
  }

  return x;  
}

