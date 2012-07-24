/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 15 Box of asterisks                                                  */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){
  int i, j;
  for(j = 0; j < atoi(argv[2]); j++){
    for(i = 0; i < atoi(argv[1]); i++)
      printf("*");
    printf("\n");
  }
  return 0;
}
