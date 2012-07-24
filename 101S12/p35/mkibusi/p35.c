/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 35:Passing a Two Dimensional Array */
/* 	   				      */
/* Approximate completion time :60min         */
/**********************************************/
#include <stdio.h>

int dim_array(int dims [][3]);
int main(int argc,char* argv[]){
  int i, j, v;
  int num [3][3];
  
  printf("\nPlease enter the values of array num ");
  for( i = 0; i < 3 ; i++){
    for(j = 0 ; j < 3 ; j++){
      printf("\nPlease enter the values for num[%d][%d]:",i,j);/*The purpose of this line to print out the row dimensional index and values of each*/
      scanf("%d",&num[i][j]);
    } }
  v = dim_array( num);
  printf("The value of sum of dimmensional array is = %d \n", v);
  return 0;
}
int dim_array(int dims [][3]){
  int i,j, sum;
  sum=0;
  
  for(i = 0; i < 3; i++){
    for(j = 0; j < 3; j++){
      sum += dims[i][j];
    }
  }
  return sum;
}
