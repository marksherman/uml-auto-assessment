/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 20: Reverse the Command Line          */
/* Approximate completion time: 20 mins          */
/*************************************************/
#include<stdio.h>
int main(int argc, char* argv[]) {
  int i;
  for(i = argc-1; i >= 0; i--){
    printf("%s\n", argv[i]);
  }
  return 0;
}
