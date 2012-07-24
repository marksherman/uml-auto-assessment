/***********************************************************************/
/* Programmer: MARTIN KIBUSI                                           */
/* 	       	      			                               */
/* Program 41:Malloc up Space for a 1-Dimensional Array of n integers  */
/* 	   				                               */
/* Approximate completion time : 20 min                                */
/***********************************************************************/
#include<stdio.h>
#include<stdlib.h>

int main(int argc,char* argv[]){
  int n, i, sum = 0;
  int *var_ptr;
  printf("Please enter the number ");
  scanf("%d",&n);
  var_ptr = (int*)malloc(n*sizeof(int));
  
  for(i = 0; i < n; i++){
    var_ptr[i]= i;
    sum += var_ptr[i];
    printf("The number is %d \n",var_ptr[i]);
  }
  printf("The sum of single dimentional number = %d \n", sum);;
  free(var_ptr);
  return 0;
}
