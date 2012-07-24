/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p30                                                                               */
/*                                                                                            */
/*  This program reads two numbers from the keyboard and stores them in to integers.          */
/*  Then the integer values are sent to another function which swaps their values and         */
/*  sends them back to the original value.                                                    */
/*                                                                                            */
/*  Approx Completion Time: 10 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>

void swap(int *a, int *b);

int main(int argc, char* argv[]){

  int x,y;

  printf("Please enter two non-negative numbers separated by a space: ");
  scanf("%d%d",&x,&y);
  printf("\n%d is stored in x and %d is stored in y.\n",x,y);
  
  swap(&x,&y);

  printf("After swapping the values around a little, %d is now stored in x, and %d is now stored in y.\n",x,y);

  return 0;
}

void swap(int *a, int *b){

  int cool;

  cool=*a;
  *a=*b;
  *b=cool;


  return;
}

