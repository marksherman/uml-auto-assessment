/**********************/
/* Betty Makovoz      */
/* Sum of Twenty      */
/* 30 minutes         */
/**********************/

#include <stdio.h>
int main(){
  int i;
  int j;
  int sum;
  FILE*fin;
  fin=fopen("testdata10","r"); 
  sum=0;
 for(j=0;j<20;j++){
    fscanf(fin,"%d",&i);
    sum=sum+i;
  }
 printf("The sum=%d\n",sum);
  fclose(fin);
  return 0;
}
