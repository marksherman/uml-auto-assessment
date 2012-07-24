/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 9: Using a for loop            */
/*                                           */
/* Estimated time of Completion: 20  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>

int main(){
  FILE *fin;
  fin=fopen("testdata9","r");
  int x; 
 for(;fscanf(fin,"%d",&x)!=EOF; printf("%d ",x)){
  }
 printf("\n");
  return(0);
}
