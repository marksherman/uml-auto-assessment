/********************************************************** */
/* Programmer: MARTIN KIBUSI                                */
/* 	       	      			                    */
/* Program 42:Malloc up Space for a Two-Dimensional Array   */
/* 	   				                    */
/* Approximate completion time : 3 hrs                      */
/************************************************************/
#include<stdio.h>
#include<stdlib.h>

int main(int argc,char* argv[]){
  int i, j, r, c,sum, sum1,sum2, row =0, column = 0;
  printf("Please enter the value of r and c \n "); 
  scanf("%d%d",&r,&c);
  int *var_ptr =(int*)malloc(r* c * sizeof(int));
  
  printf("Please enter rows and column \n");
  for(i = 0; i< r ; i++){
    for(j = 0; j < c ; j++)
      
      scanf("%d", &var_ptr[i*c + j]);
  }

  printf("Please enter the row you want to summe \n");
  
  scanf("%d",&row);
  sum =0;
  i = row;
  for(j = 0; j < c ; j++){
    sum += var_ptr[ i * c  + j];
  }
  printf("The sum of the row is %d \n", sum);
  
  
  printf("Please enter the value of column you want to sum \n");
  scanf("%d",&column);
  sum1 =0;
  j = column;
  for(i = 0; i < r; i++ ){
    sum1 += var_ptr[i * c + j];
  }

  printf("The sum of column is = %d \n",sum1);
  sum2 =0;
  j = column;
  i = row;
  for(i =0; i< r ;i++ ){
    for(j = 0; j< c; j++)
      sum2 += var_ptr[i * c +j];
  }
  printf("The sum of all array is =%d \n",sum2);
  free(var_ptr);
  return 0;
}
