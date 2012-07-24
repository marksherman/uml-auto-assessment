/*

Mike Begonis
Program p5

This program will read a user inputed number, and tell me if its greater than, or less than 100

*/


#include <stdio.h>

int main (){

  int x;
  printf("Please enter a number, any number will do.  Do it now....\n");
  scanf("%d", &x);
  
  if(x>100){
    printf("This number is bigger than 100\n");

      }
  else{
    printf("This number is not bigger than 100\n");
      }







  return 0;

}
