/******************************************/
/* Programmer: Joe LaMarca                */
/* Program: p6, Equal to zero?            */
/* Approximate time of completion: 5 min  */
/******************************************/

#include <stdio.h>
int main(int argc, char* argv[]){

  int x;

  printf("Enter a Value:");
  scanf("%d",&x);

  if (x==0)
    printf("The number is equal to zero!\n");
  else
    printf("The number is not equal to zero!\n");
  
  return 0;
}
