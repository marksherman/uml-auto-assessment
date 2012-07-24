/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 19: Argv                */
/*                                */
/*Approx. Completion Time: 15mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char*argv[]){

  int s;

  for(s=0; s<argc; s++){
  
    printf("%s\n",argv[s]);
 
  }
  return 0;

}
