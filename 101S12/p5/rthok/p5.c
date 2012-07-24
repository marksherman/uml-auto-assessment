/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 05: Bigger than 100?                                           */
/*                                                                        */
/* Approximate Time: ?? minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int x;

  printf( "\nPlease Enter A Number:" );

  scanf("%d", &x);

  if (x > 100){

    printf( "The number entered is bigger than 100.\n\n" );
  }

  else{

    printf( "The number entered is not bigger than 100.\n\n" );

  }

  return 0 ;

}
