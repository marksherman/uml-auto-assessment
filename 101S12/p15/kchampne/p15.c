/**************************************/
/* Name: Kyle Champney                */
/*                                    */
/* Program: p15                       */
/*                                    */
/* Estimated Completion Time: 15 mins */
/**************************************/

#include <stdio.h>

int main(){

  printf("Please enter two positive integers less than 21: ");

  int L;
  int H;
  scanf("%d %d", &L, &H);

  int x;
  int y;

  for(y = 0; y < H; y++)
    {
      for (x = 0; x < L; x++)
	{
	  printf("*");
	}

      printf("\n");
    }

  return 0;
}
