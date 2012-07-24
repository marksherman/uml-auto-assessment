/**********************/
/*  Danny Packard    */
/* p21 scanf returns?*/
/*   20 minutes      */
/*********************/
#include<stdio.h>
int main(int argc, char*argv[]){
  int v;
  FILE*fin;
  fin= fopen("testdata21","r");
  while(fscanf(fin,"%d",&v)!=EOF){
  printf(" %d\n",v);
  }
  fclose(fin);
  return 0;
}
