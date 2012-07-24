/***************************************************************/
/* Programmer: Harrison Kelly                                  */
/*                                                             */
/* Program 43: Square Deal                                     */
/*                                                             */
/* Approximate completion time: 2 hours                        */
/***************************************************************/

#include <stdio.h>
#include <stdlib.h>

void print_square( int N, int** array );
void spiral_movement( int initial_value, int N, int** array );
int primecheck( int initial_value );
int main( int argc, char* argv[] ){

  int N = 0;
  int initial_value = 0; 
  int** array;
  int i;

  printf("\nInput the array size now:\n");
  scanf("%d", &N);

  printf("Input the initial value:\n");
  scanf("%d", &initial_value);

  /* Creates an array of N spaces and then creates an array in that array to */
  /* make the array to store the values in, allows for two subscript         */
  /* operators to be used.                                                   */

  array = (int**)malloc( N * N * sizeof(int) );

  for( i = 0; i < N; i++ ){
    array[i] = (int*)malloc( N * sizeof(int) );
  }

  /* Fills up the array. */
  spiral_movement( initial_value, N, array );
   
  /* Prints square when full. */
  printf("\n");
  print_square( N, array);

  /* Reverses the loop to create arrays by freeing the arrays created. */
  for( i = 0; i < N; i++ ){
    free(array[i]);
  }
  /* Frees original pointer. */
  free(array);

  return 0;
}

int primecheck( int initial_value ){
  
  int i;
  int test;

  for( i = 2; i < initial_value; i++ ){
    if( initial_value % i == 0 ){
      test = 0; /* If value is not prime, return 0. */
      return test;
    }
  }
    if( test != 0)
      return 1; /* If value is prime, return 1.*/

    return 1;
}

void spiral_movement( int initial_value, int N, int** array ){

  int temp = 0;
  int j = 0;
  
  /* Instead of using random letters, I used boundary, xcoord, and ycoord to */
  /* everything organized and easier to read and understand what is going    */
  /* on in each loop.                                                        */

  int boundary = 0;
  int xcoord = (N/2);
  int ycoord =  (N/2);

    while( j < N ){
      
      /* Moves right. */
      for( boundary = 0; boundary < j; boundary++, initial_value++, xcoord-- ){
	if( (temp = primecheck( initial_value )) == 1 ){
	  array[ycoord][xcoord] = initial_value;
	}
	else{
	  array[ycoord][xcoord] = 'x';
	}
      }
      /* Moves up. */
      for( boundary = 0; boundary < j; boundary++, initial_value++, ycoord++){
	if( (temp = primecheck( initial_value )) == 1 ){
	  array[ycoord][xcoord] = initial_value;
	}
	else{
	  array[ycoord][xcoord] = 'x';
	}
      }
      /* Iterates moves. */
      j++;

      /* Moves left. */
      for( boundary = 0; boundary < j; boundary++, initial_value++, xcoord++ ){
	if( (temp = primecheck( initial_value )) == 1 ){
	  array[ycoord][xcoord] = initial_value;
	}
	else{
	  array[ycoord][xcoord] = 'x';
	}
      }
      /* Moves down. */
      for( boundary = 0; boundary < j; boundary++, initial_value++, ycoord-- ){
	if( (temp = primecheck( initial_value )) == 1 ){
	  array[ycoord][xcoord] = initial_value;
	}
	else{
	  array[ycoord][xcoord] = 'x';
	}
      }
      /* Iterates moves. */
      j++;

    }

  return;
}

void print_square( int N, int** array ){

  int i;
  int j;
 
  for( i = 0; i < N; i++ ){ 
    for( j = 0; j < N; j++ ){ 
      if( array[i][j] == 'x' ){ /* If 'x' is stored in the array, print ***. */
	printf("%-s\t", "*** ");
      }
      else if( array[i][j] != 'x' ){
        printf("%-d\t", array[i][j] );
      }
    }
    printf("\n");
  }

  return;
}

