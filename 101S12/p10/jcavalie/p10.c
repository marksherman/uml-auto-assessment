/******************/
/*John Cavalieri**/
/* p10 sum of twenty*/
/* 10 mins        */
/*****************/


#include<stdio.h>
int main(){

  int x;
  int i;
  FILE* fin;
  int sum = 0;

  fin = fopen("testdata10", "r");

  for(i = 0; i < 20; i++){

  fscanf(fin, "%d", &x);
  sum += x;
}
  fclose(fin);
  printf("%d\n", sum);
  return 0;
}
