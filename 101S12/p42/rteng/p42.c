/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p42: Malloc 2D Array                          */
/*                                                       */
/* Approximate completion time: 60 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int arc, char* argv[])
{
  int r, c, i, j, row, col, r_sum, c_sum, final_sum;
  int* malloc_array;
  printf("Enter the amount of rows and columns you would like to have in your 2D array: ");
  scanf("%d %d", &r, &c);
  /*Ask for array dimensions and store into variables for size.*/
  malloc_array = (int*) malloc(r * c * sizeof(int));
  /*Assign name malloc_array to the array function that will reserve the appropriate size of memory in the heap for user.*/
  for(i = 0; i < r; i++){
    for(j = 0; j < c; j++){
      printf("Enter the integer you want stored into array[%d][%d]: ", i, j);
      scanf("%d", &malloc_array[i*c+j]);
    }
  } 
  /*Inputs user input into specified array locations*/
  printf("What row (0 to %d) would you like summed? ", r - 1);
  scanf("%d", &row);
  for(i = 0; i < c; i++){
    r_sum = 0;
    r_sum = r_sum + malloc_array[row+row*i];
  }
  printf("The sum of all the integers in row %d is %d.\n", row, r_sum);
  /*Sums up all the integers in the specified row.*/
  printf("What column (0 to %d) would you like summed? ", c - 1);
  scanf("%d", &col);
  for(i = 0; i < r; i++){
    c_sum = 0;
    c_sum = c_sum + malloc_array[col+col*i];
  }
  printf("The sum of all the integers in column %d is %d.\n", col, c_sum);
  /*Sums up all the integers in the specified column.*/
  final_sum = 0;
  for(i = 0; i < r; i++){
    for(j = 0; j < c; j++){
      final_sum = final_sum + malloc_array[i*c+j];
    }
  }
  printf("The sum of all integers in the array is %d.\n", final_sum); 
  /*Sums up all the integers in the whole array.*/
  free(malloc_array);
  /*Deallocates the memory reserved for this program from the malloc.*/
  return 0;
}
