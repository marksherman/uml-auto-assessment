/************************************************/
/* Programmer: Joe LaMarca                      */
/* Program: the fscanf Funtion p4               */
/* Approximate time of completion: 50 min       */
/************************************************/


#include <stdio.h>
int main(){

  int x;
  FILE* fin;

  fin=fopen("testdata4","r");
  fscanf(fin, "%d", &x);
  fclose(fin);

  printf("%d\n",x);

  return 0;
}
