/********************************************************************/
/* Programmer: Joe LaMarca                                          */
/* Program: Digit Sum                                               */
/* Approximate time of completion: hours...segmentation fault       */
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

  printf("The sum is:%d", sum);

  fclose(fin);

  return 0;
}

int digitsum(int a){

  int b;
  int c;

  b=0;
  c=1;
  while(a!=0){
    b=a%10;
    c=c+b;
    b=a/10;
    a=b;
  }
  
  return c;
}
