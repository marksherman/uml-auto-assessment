/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p25                                                                               */
/*                                                                                            */
/*  This program reads a pair of positive integers less than 21 and non-negative,             */
/*  and prints an empty box of asterisks with the given length and width to the screen.       */
/*                                                                                            */
/*  Approx Completion Time: 7 minutes                                                         */
/**********************************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int L,H,i,y;
  printf("Please input two numbers less than 21 and non-negative.  Please separate these numbers by a space: ");
  scanf("%d%d",&L,&H);
  for(i=0;i<L;i++){
    printf("*");
  }
  printf("\n");
  for(i=0;i<H-2;i++){
    printf("*");
    for(y=0;y<L-2;y++){
      printf(" ");
    }
    printf("*\n");
  }
  for(i=0;i<L;i++){
    printf("*");
  }
  printf("\n");


  return 0;
}
