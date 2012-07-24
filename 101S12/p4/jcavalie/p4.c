/******************/
/*John Cavalieri  */
/* Fscaf function*/
/* 10 mins */

#include<stdio.h>
int main(){
  FILE* fin;
  int x;

  fin = fopen("testdata4", "r");
  fscanf(fin,"%d",&x);
  fclose(fin);

  printf("stored integer value in file:\n%d\n", x);

  return 0;
}

