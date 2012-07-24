/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p43: Square Deal                              */
/*                                                       */
/* Approximate completion time: 80 minutes               */
/*********************************************************/
#include <stdio.h>
int primechecker(int num);
int main(int argc, char* argv[])
{
  int i, j, increaser, N, initial_value, bound;
  int** malloc_array;
  printf("What would you like to be both the length and width of the 2D-array? (Must be an odd integer between 3 and 15.): ");
  scanf("%d", &N);
  printf("What is the initial value of the array center? ");
  scanf("%d", &initial_value);
  malloc_array = (int**) malloc(N * sizeof(int*));
  for(i = 0; i < N; i++){
    malloc_array[i] = (int*) malloc(N * sizeof(int));
  }
  /*Create an N*N array of pointers.*/
  i = N/2 + 1;
  j = i;
  /*Initial the center block.*/
  malloc_array[i][j] = initial_value;
  increaser = 1;
  /*Go right from center block until increaser value is hit.*/
  while(increaser < N){
    bound = 0;
    while(bound <= increaser){
      initial_value++;
      j++;
      malloc_array[i][j] = initial_value;
      bound++;
    }
    /*Up*/
    bound = 0;
    while(bound <= increaser){
      initial_value++;
      i--;
      malloc_array[i][j] = initial_value;
      bound++;
    }
    increaser++;
    /*Increase the increaser value so that movement stays in spiral form*/
    /*Left*/
    bound = 0;
    while(bound <= increaser){
      initial_value++;
      j--;
      malloc_array[i][j] = initial_value;
      bound++;
    }
    /*Down*/
    bound = 0;
    while(bound <= increaser){
      initial_value++;
      i++;
      malloc_array[i][j] = initial_value;
      bound++;
    }
    increaser++;
    /*Increase increaser value again because every right and up goes the same amount, and down/left goes the same amount of spaces.*/
  }
  for(i = 0; i < N; i++){
    for(j = 0; j < N; j++){
      if((primechecker(malloc_array[i][j])) == 1){
	printf("***");
      }
      else if((primechecker(malloc_array[i][j])) == 0){
	printf("%d", malloc_array[i][j]);
      }
    } 
  }
  /*Checks for primes and prints them out. If not a prime, will print out "***".*/
  free(malloc_array);
  /*Deallocate the memory used in the malloc.*/
  return 0;
}
int primechecker(int num){
  int i = 2;
  while (1){
    if(num % i == 0){
      return 1;
    }
    i++;
    if (i == num){
      return 0;
    }
  }
}
