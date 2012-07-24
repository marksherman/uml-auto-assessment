/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : The Sum of a Bunch                                           */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ) {

  int sum , y ;
  FILE *fin ;
  
  fin = fopen( "testdata22" , "r" ) ;
  for( sum = 0 ; fscanf( fin , "%d" , &y ) != EOF  ; sum += y ) ; 
  printf( "The sum of the numbers in testdata22 is %d\n" , sum ) ; 
  fclose (fin) ;
  return 0 ;
}
