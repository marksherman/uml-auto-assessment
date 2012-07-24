/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p33                                                                               */
/*                                                                                            */
/*  This program takes an integer through standard input, computes the factorial value        */
/*  using a recursive for loop and then prints it's value to the screen.                      */
/*                                                                                            */
/*  Approx Completion Time: 33 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>

int detroit(int mio);

int main(int argc, char* argv[]){
  
  int x, fact;

  printf("Please enter an integer greater than or equal to 0: ");
  scanf("%d",&x);                                                      /* Scanf reads the integer that will be factorialized  */

  fact=detroit(x);

  printf("The factorial sum of !%d is %d\n", x, fact);

  return 0;
}
/* Function detroit takes the integer value from int x in main, and computes the factorial of it
   using a recursive loop */  
int detroit(int mio){

  if(mio==1||mio==0){
    return 1;
  }else{
    return mio*detroit(mio-1);
  }

}


