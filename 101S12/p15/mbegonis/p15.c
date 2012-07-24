/*

Mike Begonis
Program p15

This program takes two non-negative integer values from the user and prints out a box of asterisks.

*/


#include <stdio.h>

int main(int argc, char* argv[]){

  int L,H,x,y;
  
  printf("Please enter a number greater than zero but less than 21.\n");
  scanf("%d",&L);
  if(L<0||L>21){
    while(L<0||L>21){
      printf("That number did not fit in the desired legnth.  Please try again.\n");
      scanf("%d",&L);
    }
  }

  printf("Please enter another number greater than zero but less than 21.\n");
  scanf("%d",&H);
  if(H<0||H>21){
    while(H<0||H>21){
      printf("That number did not fit in the desired legnth.  Please try again.\n");
      scanf("%d",&H);
    }
  }


  for(y=0;y<H;y++){
    
    for(x=0;x<L;x++){
      printf("*");
    }
    printf("\n");
  }


  return 0;
}
