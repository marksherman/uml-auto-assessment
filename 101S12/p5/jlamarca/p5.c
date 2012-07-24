/************************************************/
/* Programmer: Joe LaMarca                      */
/* Program: Bigger than 100? p5                 */
/* Approximate Completion Time: 10 min          */
/************************************************/


#include <stdio.h>
int main(){

  int x;

  printf("Enter a value:\n");  
  scanf("%d",&x);
  if (x>100)
    printf("The number is bigger than 100\n");
  else
    printf("The number is not bigger than 100\n");

  return 0;
}
