/***Danny Packard***/
/*** p9 for loop***/
/*** 25 minutes **/
#include<stdio.h>
int main(){
  int y;
  int i; 
  FILE*fin;
  fin=fopen("testdata9","r");
  for(i=0;i<5;i++){
    fscanf(fin,"%d",&y);
    printf("%d\n",y);
  }
  fclose(fin);
  return 0;
}

