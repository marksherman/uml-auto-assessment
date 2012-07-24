/****************************************************/
/* Programmer: Joe LaMarca                          */
/* Program: p7, Positive, Negative, or Zero?        */
/* Approximate time of completion: 10 min           */
/****************************************************/

#include <stdio.h>
int main (int argc, char* argv[]){

  int x;

  printf("Enter a value:");
  scanf("%d",&x);

  if (x==0)
    printf("The number is equal to zero!\n");

  if(x>0)
    printf("The number is positive!\n");
  
  if(x<0)
    printf("The number is negative!\n");

  return 0;  
}
