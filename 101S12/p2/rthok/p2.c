/*****************************************************************************/
/*                                                                           */
/* Programmer: Ravy Thok                                                     */
/*                                                                           */
/* Program 02 : The scanf Function                                           */
/*                                                                           */
/* Approximate Time: 15 minutes                                              */
/*                                                                           */
/*****************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int value;

  printf( "Please Enter a Number:\n" );

  scanf( "%d", &value );

  printf( "\n%d Was The Number Entered\n", value);

  return 0 ;

}
