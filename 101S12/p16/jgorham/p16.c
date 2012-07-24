/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 16 Character Counter                                                 */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int count = -1;                      /*Increment will always run once*/
  char nextchar;
  do{
    nextchar = getchar();              /*Read keyboard input*/
    count++;
  }while(nextchar != EOF);             /*End on EOF input*/
  printf("\nNumber of characters entered is: %d\n", count);
  return 0;
}
