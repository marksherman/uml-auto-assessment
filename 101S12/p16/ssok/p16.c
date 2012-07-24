/*Programmer: Scott Sok*/

/*Program p16: Counts Character*/

/*approximate completion time: 45 minutes*/

#include <stdio.h>

int main (int argc, char* argv[])
{

  int x;
  
  char ch;

  x = 0;

  printf("please enter the amount of charcter you wish to be counted:\n");
  while((ch = getchar()) !=EOF) {
    x++;

  }

  printf("the amount of charcter is: %i \n", x);

  return 0;

}


