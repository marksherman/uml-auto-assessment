/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 19: Argv                                                       */
/*                                                                        */
/* Approximate Time: 15 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int i;

  printf("\n");

  for(i = 0; i < argc ; i++){

    printf("%s\n", argv[i]);

  }

  printf("\n");

  return 0 ;

}
