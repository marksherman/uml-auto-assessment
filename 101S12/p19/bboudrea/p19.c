/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 19: Argv                       */
/*                                           */
/* Estimated time of Completion: 30  minutes */
/*                                           */
/*********************************************/

#include<stdio.h>
#include<stdlib.h>

int main(int argc, char* argv[]){
  int i;
  for(i=0;i<argc; ++i){
    printf("%s\n", argv[i]);
  }
  return(0);
}
