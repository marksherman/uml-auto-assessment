/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 06 : Equal to Zero?                                            */
/*                                                                        */
/* Approximate Time: 10 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int x;

  printf("\nPlease enter a number:");

  scanf("%d", &x);

  if ( x == 0){

    printf ("\nThe number is equal to zero.\n\n");
  }

  else { 
    
    printf ("\nThe number is not equal to zero.\n\n"); 
 
  }

  return 0 ;

}
