/**********************/
/*  Danny Packard     */
/* p22 sum of a bunch */
/*   too long         */
/**********************/
#include<stdio.h>
int main(int argc, char*argv[]){
  int v;
  int sum=0;
  FILE*fin;
  fin= fopen("testdata22","r");
  while(fscanf(fin,"%d",&v)!=EOF){
    sum += v;
  }
  printf("%d\n",sum);
  fclose(fin);
  return 0;
}
