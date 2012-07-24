/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 20: Reverse the Command Line                                   */
/*                                                                        */
/* Approximate Time: 15 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int i;

  printf("\n");

  for(i = argc - 1 ; i >= 0 ; i--){

    printf("%s\n", argv[i]);

  }

  printf("\n");

  return 0 ;

}
