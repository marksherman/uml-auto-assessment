/*************************************************/
/* Programmer: Joe LaMarca                       */
/* Program: Recursive digit sum                  */
/* Approximate time of completion: 1 hour        */
/*************************************************/

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

  if(a<10)
    return 1;
  else
    return a%10 + digitsum(a/10);
}
