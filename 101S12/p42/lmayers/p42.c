/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Malloc up space for a 2- Dimensional Array                   */
/*                                                                       */
/* Approximate completion time:                                          */
/*************************************************************************/
#include <stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] ) {

  int row, column, r,c, i, j;
  int sumrow =0, sumcol =0, sum=0;
  int * A;

  printf("Please enter the size for the row and column:\n");
scanf("%d %d", &row ,&column); 
 A = (int *)  malloc ( sizeof(int) * row * column);

 for( i = 0; i < row; i++)
   for( j = 0; j < column; j++){
     printf("Please enter integers for each element :\n");
     scanf("%d", &A [( i * row) + j]);
   }    
 
 printf("Please enter the row you are trying to locate:\n");
     scanf("%d", &r);
	   for( j= 0; j < column; j++)
       sumrow += A [(r * row) + j];
   
   printf("%d\n", sumrow);
 
   printf("Please enter the column you are trying to locate:\n");
   scanf("%d", &c);
   for( i = 0; i < row; i++)
     sumcol += A[(i * row) + c];

 printf("%d\n", sumcol);
 
   for( i = 0; i < row; i++)
     for( j = 0; j < column; j++){

       sum += A [(i * row)+ j];
     }    
   printf("The sum of the entire array: %d\n", sum);
     
       
  return 0;
}  
