/************************************************/
/* Programmer: Joe LaMarca                      */
/* Program: Reverse                             */
/* Approximate time of completion: 30 min       */
/************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int array[10];
  int x;

  printf("Type ten numbers,hit enter, then generate EOF(ctrl+d):");
 
  for(x=10;scanf("%d",&array[x])!=EOF;x--);

  printf("These reversed are:");

  for(x=1;x<=10;x++)
    printf("%d ",array[x]);
 
  printf("\n");

  return 0;
}
