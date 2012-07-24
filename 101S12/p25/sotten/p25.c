/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 25: Hollow block of *   */
/*                                */
/*Approx. Completion Time: 20mins */
/*                                */
/**********************************/

#include <stdio.h>

int main(int argc, char*argv[]){

  int H, L, a, b;

  printf("Choose values for L,length, and H,height, of block\n");
  scanf( "%d%d",&H,&L);
  for(b= 0; b<L; b++){ /* for loop for the lenth*/
    for (a=0; a<H; a++) /* for loop for the height*/
      if((a==0)||(a==(H-1))||(b==0)||(b==(L-1))) /* tells whether to print * or space*/
	printf("*");
      else printf(" "); /* prints the space*/

  printf("\n"); /* prints on new line*/
  
  }
return 0;
}
