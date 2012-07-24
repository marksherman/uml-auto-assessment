/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 27 : Reverse                                                   */
/*                                                                        */
/* Approximate Completion Time: 40 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int x [10];
  int i=0;

  printf("\nPlease enter ten numbers:\n\n");

  while(i<10){
    scanf("\n%d", &x[i]);
    i++;
  }   

  printf("\nThe numbers in reverse is: ");

  for(i=9; i>=0; i--){
      printf("%d ",x[i]);
    }
 
  printf("\n\n");

  return 0 ;

}
