/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Argv                                                         */
/*                                                                           */
/* Approximate Completion Time : 3 minutes                                   */
/*****************************************************************************/

#include <stdio.h> 

int main( int argc , char* argv[] ) {

  int i ;
  for( i=0 ; i<argc ; i++ ) {
    printf(argv[i]) ; 
    putchar('\n') ;
  }
  return 0 ; 
}
