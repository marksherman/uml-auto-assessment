/***Danny Packard***/
/*** p10 sum of 20*/
/*** 20 minutes ***/
#include<stdio.h>
int main(){
  int x;
  int i;
  int sum;
  FILE*fin;
  fin=fopen("testdata10","r");
  sum=0;
  for(i=1; i<=20; i += 1){
  fscanf(fin,"%d",&x);
  sum += i;
  printf("sum=%d\n",sum);
  }
  printf("\n");
  fclose(fin);
  return 0;
}
  
