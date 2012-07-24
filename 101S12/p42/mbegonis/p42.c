/***********************************************/
/*                                             */
/*  Mike Begonis                               */
/*  Program p42                                */
/*                                             */
/*  This program creates a multi-dimentional   */
/*  array with a size specified by the user    */
/*  through standard input.  The user then     */
/*  specifies a row and column which the       */
/*  program will then sum separately and print */
/*  to the screen.                             */
/*                                             */
/*  Approx Completion Time: 30 minutes         */
/***********************************************/

#include <stdlib.h>
#include <stdio.h>

int main(int argc, char* argv[]){
  
  /*  Int r is the row size.  r is the row size.
   *  size is the overal size of the array.  
   *  i and j are incremental integers.
   *  sum_row and sum_column are the sums of the
   *  specified row and column.  int *array is the
   *  array where the integers are stored.
   */
  int r,c,size,i,j,sum_row=0,sum_column=0;
  int *array;
  
  printf("Please enter the number of rows your array will be: ");
  scanf("%d",&r);
  
  printf("Please enter the number of columns your array will be: ");
  scanf("%d",&c);
  
  array=(int*)malloc(r*c*sizeof(int));
  
  size=r*c;
  
  printf("Please input %d integers: ",size);
  /*  This for loop scans the user inputed integers
   *  into the array in the pattern of a two dimentional
   *  array.
   */
  for(i=0;i<r;i++){
    for(j=0;j<c;j++){
      scanf("%d",&array[i*c+j]);
    }
  }
  
  printf("Please enter which row you would like summed: ");
  scanf("%d",&i);
  /*  This for loop adds together the values in the 
   *  specified row in the array.
   */
  for(j=0;j<c;j++){
    sum_row+=array[(i-1)*c+j];
  }
  printf("The sum of row %d is %d.\n",i,sum_row);
  
  printf("Please enter which column you would like summed: ");
  scanf("%d",&j);
  /*  This for loop adds together the values in the 
   *  specified column in the array.
   */
  for(i=0;i<r;i++){
    sum_column+=array[(j-1)*r+j];
  }
  printf("The sum of column %d is %d.\n",j,sum_column);
  
  free(array);
  
  return 0;
}


