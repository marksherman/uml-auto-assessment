/*                                  */
/* Programmer: Rachel Driscoll      */
/*                                  */
/* Title: Sine Function             */
/*                                  */
/* Approx Completion Time: 30 min   */
/*                                  */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc, char * argv[]){

  float i;

  printf( "Argc is %f\n", argc);
  i = atof(argv[1]);
  i = sin (i);
    
    printf( "Argv [%d]contains %f\n", (i));

  return 0;
}
