/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 23: fgetc and toupper          */
/*                                           */
/* Estimated time of Completion: 20  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>
#include<ctype.h>

int main(int argc, char *argv[]){
  int x,y;
  FILE *fin;
  fin=fopen("testdata23","r");
  while((x=fgetc(fin))!=EOF){
    y=toupper(x);
    putchar(y);
  }
  fclose(fin);
  return(0);
}
