/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Using a for loop                                             */
/*                                                                           */
/* Approximate Completion Time : 10 mintues                                  */
/*****************************************************************************/

#include <stdio.h>

int main() {

  int x , y ;
  FILE *fin ;
  fin = fopen( "testdata9" , "r" ) ;
  printf( "The numbers in testdata9 are :\n" ) ;
  for(x=0 ; x<5 ; x++) {
    fscanf(fin, "%d" , &y) ;
    printf( "%d\n" , y ) ;
  }
  fclose( fin ) ;
  return 0 ;
}
