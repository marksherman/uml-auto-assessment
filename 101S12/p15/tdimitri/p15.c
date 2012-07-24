/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 15: Solid Box of Asterisks            */
/* Approximate completion time: 25 mins          */
/*************************************************/
#include <stdio.h>
int main(int argc, char* argv[]) {
  int L, H, i, x;
  printf("Enter two non-negative integer values less than 21:");
  scanf( "%d%d", &L, &H);
  for(x=0; x<H; x++){
    for(i=0; i<L; i++){
      printf("*");
    }
    putchar('\n');
  }
  putchar('\n');
  return 0;
}
