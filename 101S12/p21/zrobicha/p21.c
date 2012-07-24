/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Scanf Returns What?                                          */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int main (int argc, char* argv[] ) {

  FILE *fin ; 
  int numbers ;

  fin = fopen( "testdata21" , "r" ) ;
  while ( fscanf( fin , "%d" , &numbers )  != EOF ) {
    printf( "%d\n" , numbers ) ;
  }
  fclose (fin) ;
  return 0 ; 
}
    
