/*

Mike Begonis
Program p7

This program takes a user inputed number and determines whether it is positive, negative, or equal to zero

*/


#include <stdio.h>

int main (int argc, char* argv[]){

  int x;
  printf("Please enter any number that is either positive or negative or even in between....like a boss\n");
  scanf("%d", &x);

  if(x==0){
    printf("The number is zero.\n");

  }
  else{
    if(x>0){
      printf("The number is positive.\n");
    }
    else{
      printf("The number is negative.\n");
    }
  }







  return 0;

}
