/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 33: Recursive Factorial         */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
  
int sum( int array[3][3] );
int main(int argc, char* argv[])
{
  int array[3][3];
  int results = 0;

  printf( "Please enter 9 integers \n" );
  
  results = sum(array);
  printf("The sum is %d \n", results);
  
  return 0;
}
int sum( int array[3][3] ){

  int ans = 0;
  int i, j;
 
    for( i=0; i<3; i++ ){
      for( j=0; j<3; j++ ){
	scanf( "%d", &array[i][j] );
	ans +=array[i][j];
      }
    }
    return ans;
}
