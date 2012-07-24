/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 06 : Positive, Negative, or Zero?                              */
/*                                                                        */
/* Approximate Time: 15 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int x;

  printf("\nPlease enter a number:");

  scanf("%d", &x);

  if ( x == 0 ){

    printf ("\nThe number is equal to zero.\n\n");
  }

  else if ( x > 0 ){ 
    
    printf ("\nThe number is positive.\n\n"); 
 
  }

  else if ( x < 0 ){

    printf ("\nThe number is negative.\n\n");

  }

  return 0 ;

}
