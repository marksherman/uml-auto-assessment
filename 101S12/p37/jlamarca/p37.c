/********************************************************************/
/* Programmer: Joe LaMarca                                          */
/* Program: Digit Sum                                               */
/* Approximate time of completion: 2 hours                          */
/********************************************************************/

#include <stdio.h>

int digitsum(int a);

int main(int argc, char* argv[]){

  int x;
  int sum;
  FILE* fin;

  fin=fopen(argv[1],"r");
  fscanf(fin,"%d",&x);

  sum=digitsum(x);

  printf("The sum is:%d\n", sum);

  fclose(fin);

  return 0;
}

int digitsum(int a){

  int b;

  b=0;
  while(a>0){
    b+=a%10;
    a=a/10;
  }
  
  return b;
}
