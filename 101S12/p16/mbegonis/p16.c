/*

Mike Begonis
Program p16
This program counts the number of characters entered by the user until it reaches EOF.  It then returns the number of characters entered.

*/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x,y=0;

  while((x=getchar())!=EOF){
    y++;
    }
  printf("\n%d\n",y);

  return 0;
}
