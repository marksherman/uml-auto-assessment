/******************************************************/
/* Programmer: Joe LaMarca                            */
/* Program: p30, simulating a call by reference       */
/* Approximate time of completion: 15 min             */
/******************************************************/

#include <stdio.h>
#include <stdlib.h>

void swap(int *a, int *b);

int main(int argc, char* argv[]){

  int x;
  int y;

  x=atoi(argv[1]);
  y=atoi(argv[2]);
 
  swap(&x,&y);

  printf("%d %d\n",x, y);

  return 0;
}

void swap(int *a, int *b){

  int temp;

  temp=*a;
  *a=*b;
  *b=temp;

  return;
}
