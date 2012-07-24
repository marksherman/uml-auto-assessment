/*

Mike Begonis
Program p6

This program nicely asks a user to input a single integer and determines if its equal to zero or not.

*/


#include <stdio.h>

int main (int argc, char* argv[]){

  int x;
  printf("Please enter a number, any number will do.  Do it now.... Please....\n");
  scanf("%d", &x);

  if(x==0){
    printf("The number is equal to zero.\n");

  }
  else{
    printf("The number is NOT equal to zero.\n");
  }







  return 0;

}
