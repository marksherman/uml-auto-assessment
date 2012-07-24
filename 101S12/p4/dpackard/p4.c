/***Danny Packard***/
/*** p4 fscanf******/
/*** 20 minutes*****/
#include<stdio.h>
int main(){
  int v;
  FILE*fin;
  fin= fopen("testdata4","r");
  fscanf(fin,"%d",&v);
  fclose(fin);
  printf("The number in the file testdata4:%d\n",v);
  return 0;
}
