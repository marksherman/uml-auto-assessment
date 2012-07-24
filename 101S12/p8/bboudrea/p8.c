/**************************************************/
/*                                                */
/* Programmer: Brian Boudreau                     */
/*                                                */
/* Assignment 8: One horizontal line of asterisks */
/*                                                */
/* Estimated time of Completion: 15 minutes       */
/*                                                */
/**************************************************/

#include<stdio.h>

int main(){
  int value;
  FILE *fin;
  fin=fopen("testdata8","r");
  fscanf(fin,"%d",&value);
  int x;
  for(x=1; x<=value; x++){
    printf("*");
      }
  printf("\n");


  return(0);
}
