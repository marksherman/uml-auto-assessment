/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 41 Malloc 1d array                                                */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){
  int n = 0;
  int i = 0;
  int sum = 0;
  int input = 0;
  int* array;
  printf("Please enter the size of the array to create: ");
  scanf("%d", &n);                                             /*Get size*/
  array = (int*) malloc(n*sizeof(int));                        /*Allocate memory*/
  printf("Please populate array:\n");                          /*Populate*/
  for( i = 0 ; i < n ; i++){
    scanf("%d", &input);
    array[i] = input;
  }
  for( i = 0 ; i < n ; i++)
    sum = sum + array[i];
  printf("The sum of the array is %d\n", sum);
  free(array);
  return 0;
}
