/*****************************************************/
/* Programmer: Joe LaMarca                           */
/* Program: The abs function p11                     */
/* Approximate time of completion: 10 min            */
/*****************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int x;

  printf("Enter a value:");

  scanf("%d",&x);
  x=abs(x);

  printf("%d\n",x);

  return  0;
}
