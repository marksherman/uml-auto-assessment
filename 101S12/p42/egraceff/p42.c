/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Malloc up Space for a Two-Dimensional Array                      */
/*                                                                           */
/* Approximate completion time: 1 hour 15 minutes                            */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
int main( int argc, char *argv[] )
{
  int* ptr;
  int r, c, i, j, sumrow = 0, sumcolumn = 0, sumall = 0;
  printf("\nPlease enter the number of rows and columns in the array respectively: \n");
  /*prompts the user for the length and width of the array */
  scanf("%d %d", &r, &c);
  /* stores the length and width of the array into the variables r and c */
  ptr = (int*)malloc(r*c*sizeof(int));
  /* uses malloc to allocate memory for an array of r rows and c columns */
  printf("\nPlease enter the %d integers to fill the array:\n", (r*c));
  /* prompts the user for the values to be stored into said array */
  for (i=0; i<r; i++){
    for (j=0; j<c; j++){
      scanf("%d", &ptr[i*c +j]);
      /* scans integers into the array */
    }
  }

  printf("\nPlease specify the number of which row (0 to r-1) you wish to sum:\n");
  /* prompts the user for the number row that will be summed */
  scanf("%d", &i);
  /* stores this value in i */
  for (j=0; j<c; j++){
    sumrow = sumrow + ptr[i*c + j];
    /* takes the sum of the values in the given row, i, as the loop iterates 
       through the values in each column of the given row */
  }
  printf("\nThe sum of the values in row %d is %d.\n", i, sumrow);
  /* prints out the sum of the integers in the given row found from the above
     loop */

  printf("\nPlease specify the number of which column (0 to c-1) you wish to sum:\n");
  /* prompts the user for the number column that will be summed */
  scanf("%d", &j);
  /* stores this value in j */
  for (i=0; i<r; i++){
    sumcolumn = sumcolumn + ptr[i*c + j];
    /* takes the sum of the values in the given column, j, as the loop iterates
       through the values in each row of the given column */
  }
  printf("\nThe sum of the values in column %d is %d.\n", j, sumcolumn);
  /* prints out the sum of the integers in the given column found from the 
     above loop */

  for(i= 0; i<r; i++){
    for(j=0; j<c; j++){
      sumall = sumall + ptr[i*c + j];
      /* iterates through each of the cells in the array adding up all of the
         values in each row, column by column */
    }
  }
  printf("\nThe sum of all of the values in the array is %d.\n", sumall);
  /* prints the sum of all of the values in the array */

  free(ptr);
  /* deallocates the memory previously allocated for the array */
  return 0 ;
}
