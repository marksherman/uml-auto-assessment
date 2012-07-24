/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Sum of twenty                                                */
/*                                                                           *//* Approximate Completion Time : 10 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int main() {

  int x , y , z=0 ;
  FILE *fin ;
  fin = fopen( "testdata10" , "r" ) ;
  printf( "The sum of the numbers in testdata10 is :\n" ) ;
  for( x=0 ; x<20 ; x++ ) {
    fscanf(fin, "%d" , &y) ;
    z = y + z ;
  }
  fclose(fin) ;
  printf( "%d\n" , z ) ;
  return 0 ;
}

   
