/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 33: Recursive Factorial	      */
/* 	   				      */
/* Approximate completion time : 10 min       */
/**********************************************/

#include <stdio.h>

int fact(int a);
int main(int argc,char* argv[]){
  int x, y;
  printf("\nPlease enter the factorial number = ");
  scanf("%d",&x);
  y = fact(x);
  printf("The value of factorial number is = %d \n\n", y);
  
  return 0;
}
int fact(int a){
  
  if( a == 1){
    return 1;
  }
  else
    return a * fact(a - 1);
}
