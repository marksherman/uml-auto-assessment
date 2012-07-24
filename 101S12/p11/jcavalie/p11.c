/****************************/
/*John Cavalieri           */
/*p11 the abs function    */
/*5 mins                  */
/*************************/
#include<stdio.h>
#include<stdlib.h>

int main(){

  int x;
  int y;
  
  printf("Enter any integer value inlcluding negatives:\n");
  scanf("%d", &x);

  y =  abs(x);

  printf("The absolute value of the integer is: %d\n", y);

  return 0;
}

  
