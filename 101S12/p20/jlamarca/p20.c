/********************************************/
/* Programmer: Joe LaMarca                  */
/* Program: reverse the command line        */
/* Approximate time of completion: 10 min   */
/********************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x;

  for(x=argc-1;x>=0;x--)
    printf("%s\n",argv[x]);
  
  return 0;
}
