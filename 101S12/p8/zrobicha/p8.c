/*****************************************************************************/
/* Programming : Zachary Robichaud                                           */
/*                                                                           */
/* Assignment : One horizontal line of asterisks                             */
/*                                                                           */
/* Approximate completion time : 20 minutes                                  */
/*****************************************************************************/


#include <stdio.h>

int main() {
  
  int z, y ;
  FILE *fin ;
  fin = fopen( "testdata8" , "r" ) ;
  fscanf(fin, "%d", &z) ;
  for(y=0 ; y<z ; y++) {
    printf( "*" ) ;
  }
  fclose(fin) ;
  printf( "\n" ) ;
  return 0 ;

}
