/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 21: scanf returns what?        */
/*                                           */
/* Estimated time of Completion: 25  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>

int main(int argc, char *argv[]){
  int x;
  FILE *fin;
  fin=fopen("testdata21","r");
  while(fscanf(fin,"%d",&x)!=EOF){
    printf(" %d",x);
  }
  fclose(fin);
  printf("\n");
  return(0);
}
