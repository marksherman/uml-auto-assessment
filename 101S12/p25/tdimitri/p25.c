/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 25: Empty Box of Asterisks            */
/* Approximate completion time: 95 mins          */
/*************************************************/

#include <stdio.h>

int main(int argc, char* argv[]) {
  int L, H, i, x;
  printf("Enter two non-negative integer values less than 21:\n");
  scanf( "%d%d", &L, &H);
  for( x = 0; x < H; x++){
    for(i = 0; i < L; i++){
      if(( i == (L-1) ) || ( i == 0 ) || ( x == (H-1) ) || ( x == 0 ))
	printf("*");
      else{
	printf(" ");
      }
    }
    putchar('\n');
  }
  return 0;
}
