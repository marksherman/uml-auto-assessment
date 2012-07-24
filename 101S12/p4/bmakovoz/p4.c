/* Betty Makovoz */
/* The fscanf Funtion */
/* 20 min */

#include<stdio.h>
int main(){
  int x;
  FILE*fin;
  fin=fopen("testdata4","r");
    fscanf(fin,"%d",&x);
  fclose(fin);
  printf("Number found in textdata4:%d\n",x);
  return 0;
}
