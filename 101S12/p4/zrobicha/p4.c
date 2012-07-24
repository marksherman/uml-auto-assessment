/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignemt : The fscanf Function                                           */
/*                                                                           */
/* Approximate Completion Time : 10 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int main() {

  int x ;
  FILE *fin ;
  fin = fopen( "testdata4", "r") ;
  fscanf(fin, "%d", &x) ;
  printf( "The number in the file testdata4 is\n%d\n" , x ) ;
  fclose(fin) ;

  return 0 ;
}
