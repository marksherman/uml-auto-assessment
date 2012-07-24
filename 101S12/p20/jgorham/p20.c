/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 20: Reverse the command line                                         */
/*                                                                              */
/* Approximate Completion Time:  1 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int i;                           /*Originally just used argc as for conditions style sheet seemed to discourage this.*/
  for(i = argc - 1; i >= 0; i--)
    printf("%s\n", argv[i]);
  return 0;
}
