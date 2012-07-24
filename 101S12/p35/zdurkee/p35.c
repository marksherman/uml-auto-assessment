/***********************************************************/
/*  Programmer: Zachary Durkee                             */
/*                                                         */
/*  Program 35: Passing a Two Dimensional Array            */
/*                                                         */
/*  Approximate completion time: 1 hour                    */
/***********************************************************/

#include <stdio.h>

int sum( int matrix[][3] );

int main( int argc, char * argv[] ){

  int i, j, valuesum, value[3][3];

  for( i=0; i<3; i++ ){

    for( j=0; j<3; j++ ){

      scanf( "%d", &value[i][j] );

    }

  }

  valuesum = sum( value );

  printf( "%d\n", valuesum );

  return 0;

}

int sum( int matrix[][3] ){

  int i, j, addvalue = 0;

  for( i=0; i<3; i++ ){ 

    for( j=0; j<3; j++ ){

      addvalue += matrix[i][j];

    }

  }

  return addvalue;

}
