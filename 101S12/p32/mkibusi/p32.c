/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 32:Non-recursive Factoria	      */
/* 	   				      */
/* Approximate completion time : 30 min       */
/**********************************************/
#include <stdio.h>

int fact(int a);
int main(int argc,char* argv[]){
  int num, y;
  
  printf("\nPlease enter the factorial number = ");
  scanf("%d",&num);
  y = fact(num);
  printf("The value of factorial number = %d \n\n", y);
  return 0;
}
int fact(int a){
  int i, x;
  x = 1;
  for(i = 1; i <= a; i++){
    x = x * i;
  }
  return x;
}
