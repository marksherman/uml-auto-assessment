/********************************************/
/*                                          */
/* Programmer: Brian Boudreau               */
/*                                          */
/* Assignment 4: The fscanf function        */
/*                                          */
/* Estimated time of Completion: 30 minutes */
/*                                          */
/********************************************/

#include<stdio.h>

int main(){
  int value;
  FILE *fin;
  fin=fopen("testdata4","r");
  fscanf(fin,"%d",&value);
  printf("%d\n",value);
  fclose(fin);

  return(0);
}
