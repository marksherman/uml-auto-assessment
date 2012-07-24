/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 24: Average                    */
/*                                           */
/* Estimated time of Completion: 30  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>

int main(int argc, char *argv[]){
  int x=0;
  float sum=0,  avg=0;
  FILE *fin;
  fin=fopen("testdata24","r");
  while(fscanf(fin,"%d",&x)!=EOF){
    sum=sum+x;
  }
  fclose(fin);
  avg=sum/4;
  printf("sum=%e\n",sum);
  printf("average=%e\n",avg);
  return(0);
}
