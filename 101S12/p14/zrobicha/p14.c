/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignments : Sine function                                               */
/*                                                                           */
/* Approxiamte Copletion Time : 20 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc , char* argv[] ) {

  float x ;
  x = atof(argv[1]) ;  
  x = sin(x) ;
  printf( "The sine of %s is %e\n" , argv[1] , x ) ;
  return 0 ;
}
