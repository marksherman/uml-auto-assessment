/*************/
/*John Cavalieri*/
/*bigger than 100?*/
/* 5 mins*/

#include<stdio.h>
int main(){

  
  int x;
  FILE* fin;

  fin = fopen("testdata4", "r");
  fscanf(fin,"%d", &x);
  fclose(fin);

  if(x < 100){

    printf("the value is not bigger than 100\n");
  }
  else

    printf("the value is bigger than 100\n");

  return 0;
}
