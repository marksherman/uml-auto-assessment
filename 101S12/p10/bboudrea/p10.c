/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 10: Sum of Twenty              */
/*                                           */
/* Estimated time of Completion: 20  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>

int main(){
  FILE *fin;
  fin=fopen("testdata10","r");
  int sum,x; 
  for(x=0;fscanf(fin,"%d",&x)!=EOF;){
    sum+=x;
  }
  printf("%d\n",sum);
  return(0);
}
