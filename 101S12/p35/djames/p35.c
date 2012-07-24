/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 35: Passing a Two Dimensional Array */
/*                                            */
/*Approximate completeion time:  minutes      */
/**********************************************/

#include <stdio.h>

int sum( int vector[][3] );

int main(int argc, char* argv[]){  

  int array[3][3];

  int x, y;

  printf( "enter nine numbers\n" );

  for( x=0; x<3; x++ ){

    for( y=0; y<3; y++ ){

      scanf( "%d", &array[x][y] );
    }
  }

  x = sum( array );

  printf( "The sum of the nine numbers is: %d\n", x );

  return 0;
}

int sum( int vector[][3] ){

  int x, y;

  int total = 0;

  for( x=0; x<3; x++ ){

    for( y=0; y<3; y++ ){

      total = total + vector[x][y];
    }
  }

  return total;
}
