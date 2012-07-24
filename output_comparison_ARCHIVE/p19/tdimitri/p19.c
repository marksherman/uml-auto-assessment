/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 19: Argv                              */
/* Approximate completion time: 20 mins          */
/*************************************************/
#include<stdio.h>
int main(int argc, char* argv[]) {
  int i;
  for(i=0; i<argc; i++){
    printf("%s\n", argv[i]);
  }
  return 0;
}
