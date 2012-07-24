/****************************************************************/
/* Programmer: Yasutoshi Nakamura                               */
/*                                                              */
/* Program 20: Reverse the Command Line                         */
/*                                                              */
/* Approximate completion time: 20 minutes                      */
/****************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int i;

  for( i = 0; i < argc; i++ ) {
    printf( "%s\n", argv[ argc - ( 1 + i ) ] );
  }

  return 0;

}
