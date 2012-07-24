/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 27:Reverse			      */
/* 	   				      */
/* Approximate completion time : 20 min       */
/**********************************************/

#include <stdio.h>

int main(int argc,char* argv[]){
  int i,x; 
  int num[10];
  
  printf("Please Enter ten intergers,the code will reverse the integers \n ");
 
  for(i = 0; i < 10; i++){
    scanf("%d", &x);
    num[i] = x ;
    printf(" %d ", 11 - num[i]);
  }
  printf("\n");
  return 0;
}
