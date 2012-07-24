/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p27                                                                               */
/*                                                                                            */
/*  This program reads 10 integers from the keyboard using scanf and then prints out the      */
/*  numbers to the screen in reverse order.                                                   */
/*                                                                                            */
/*  Approx Completion Time: 5 minutes                                                         */
/**********************************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x[10],i;
  
  printf("Please enter 10 integers.  Please press enter after each integer.\n");
  for(i=0;i<10;i++){
    scanf("%d",&x[i]);
  }
  for(i=9;i>=0;i--){
    printf("%d\n",x[i]);
  }

  return 0;
}
