/**************************/
/*   Betty Makovoz        */
/* Solid Box of Asterisks */
/*   15 minutes           */
/**************************/

#include <stdio.h>

int main(int argc, char*argv[]) {
  int L;
  int H;
  int x,y;
  printf("Choose L and H\n");
  scanf( "%d%d",&H,&L);
  for(y= 0; y<L; y++){
 for (x=0; x<H; x++){
   printf("*");
 }
  printf("\n");
  }
  return 0;
  }
