/*

Mike Begonis
Program p11

This program takes in a number from the keyboard and prints out its absolute value using the absolute value function.

*/


#include <stdio.h>
#include <stdlib.h>

int main (int argc, char*argv[]){

  int x;
  printf("Please enter any positive or negative number immediatly now.  You can also enter zero if you want.  If thats how you roll.\n");
  scanf("%d", &x);

  x=abs(x);

  printf("The absolute value of this number is %d\n", x);



  return 0;

}
