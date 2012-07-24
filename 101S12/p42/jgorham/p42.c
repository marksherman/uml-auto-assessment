/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 42: Malloc a 2d array                                                */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(){
  int r = 0;
  int c = 0;
  int* array;
  int i = 0;
  int nin = 0;
  int sum_r = 0;
  int sum_c = 0;
  int sum_a = 0;
  printf("Please enter number of rows followed by number of columns:\n");               /* Prompt user for input*/
  scanf("%d", &r);
  scanf("%d", &c);
  array = (int*) malloc( r * c * sizeof(int) );
  printf("Please populate array:\n");
  for( i = 0 ; i < (r * c) ; i++ ){
    scanf("%d", &nin);
    array[i] = nin;
  }

  /*Row section*/
  printf("Please choose row to sum: ");
  scanf("%d", &nin);                                                                  /*Reusing nin here*/
  for( i = (nin * c) ; i < ( (nin + 1) * c) ; i++ )
    sum_r = sum_r + array[i];
  printf("Sum of row %d is %d.\n", nin, sum_r);

  /*Column Section*/
  printf("Please choose column to sum: ");
  scanf("%d", &nin);
  for( i = (nin + c) ; i < r * c ; i = (i + c) )
    sum_c = sum_c + array[i];
  printf("Sum of column %d is %d.\n", nin, sum_c);
  
  /*Array Section*/
  for( i = 0 ; i < (r * c) ; i++ )
    sum_a = sum_a + array[i];
  printf("Sum of the arrayt is %d.\n", sum_a);

  return 0;
}
