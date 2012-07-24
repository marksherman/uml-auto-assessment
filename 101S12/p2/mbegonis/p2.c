/*************************************************************************/
/*  Mike Begonis                                                         */
/*  Project: p2.c                                                        */
/*                                                                       */
/*  This program prints out a user inputed number as the number of the   */
/*  day for all the kiddies out there.                                   */
/*************************************************************************/


#include <stdio.h>

int main() {

  int x;
  
  printf("What is the number of the day kids? \n");
  scanf("%d",&x);

  printf( "Thats right kids!  The number of the day is %d! \n", x);

  return 0;
}
