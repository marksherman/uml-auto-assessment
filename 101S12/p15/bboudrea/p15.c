/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 15: Solid box of Asterisks     */
/*                                           */
/* Estimated time of Completion: 20  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>
#include<stdlib.h>

int main(int argc, char* argv[]){
  int l;
  int h;
  int x;
  int y;
  l=atoi(argv[1]);
  h=atoi(argv[2]);

  for(y=1; y<=h; y++){
    for(x=1; x<=l; x++){
    printf("*");
    }
  printf("\n");
  }

  return(0);
}
