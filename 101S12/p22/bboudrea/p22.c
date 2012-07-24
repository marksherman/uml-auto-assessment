/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 22: Sum of a Bunch             */
/*                                           */
/* Estimated time of Completion: 10  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>

int main(int argc, char *argv[]){
  int x,y=0;
  char a,b;
  a='+';
  b='=';
  FILE *fin;
  fin=fopen("testdata22","r");
  while(fscanf(fin,"%d",&x)!=EOF){
    printf("%d%c",x,a);
    y=y+x;
  }
  fclose(fin);
  printf("%c%d\n",b,y);
  return(0);
}
