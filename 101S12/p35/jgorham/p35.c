/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 35 passing 2d array                                                  */
/*                                                                              */
/* Approximate Completion Time: 35 min                                          */
/********************************************************************************/

#include <stdio.h>

int sum(int array[3][3], int row, int column);

int main(int argc, char* argv[]){
  int array_in[3][3];
  int i = 0;
  int j = 0;
  int row = 0;
  int column = 0;
  int sum_out = 0;
  printf("Input # of rows: ");
  scanf("%d", &row);
  printf("Input # of columns: ");
  scanf("%d", &column);
  printf("Populate array:\n");
  for(i = 0 ; i < 3 ; i++)
    for(j = 0 ; j < 3 ; j++)
      scanf("%d", &array_in[i][j]);
  sum_out = sum(array_in[3][3], row, column);          /* Can't figure out how to */
  printf("Sum of the array is %d\n", sum_out);         /* stop warning           */
  return 0;
}

int sum(int array[3][3], int row, int column){
  int sum = 0;
  int i = 0;
  int j = 0;
  for(i = 0 ; i < row ; i++)
    for(j = 0 ; j < column ; j++)
      sum = sum + array[i][j];
  return sum;
}
