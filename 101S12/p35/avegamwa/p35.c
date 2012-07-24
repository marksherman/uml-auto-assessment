/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p35: Two-Dimensional Arrays    */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <stdio.h>

int sum( int array[3][3] );
int main(int argc, char* argv[])
{
  int array[3][3];
  int answer = 0;

  printf( "Please enter 9 integers: \n" );

  answer = sum(array);
  printf("The sum of the matrix is: %d \n", answer);

  return 0;
}
int sum( int array[3][3] ){

  int res = 0;
  int i, j;

  for( i=0; i<3; i++ ){
    for( j=0; j<3; j++ ){
      scanf( "%d", &array[i][j] );
      res += array[i][j];
    }
  }
  return res;
}
