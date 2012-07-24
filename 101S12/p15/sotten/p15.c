/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 15: Solid block of *    */
/*                                */
/*Approx. Completion Time: 10mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char*argv[]){

  int H, L, a, b;

    printf("Choose values for L,length, and H,height, of block\n");
    scanf( "%d%d",&H,&L);
    for(b= 0; b<L; b++){
      for (a=0; a<H; a++){
	printf("*");
      }
      printf("\n");
    }

  return 0;

}
