/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Reverse the command line                                     */
/*                                                                           */
/* Approximate Completion Time : 3 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {
  int i ; 
  for( i=argc-1 ; i>=0 ; i-- ) {
    printf(argv[i]) ;
    printf( "\n" ) ;
  }
  return 0 ;
}
