/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Unfilled Box                                                 */
/*                                                                           */
/* Approximate Completion Time : 15 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int main( int argc , char* argv[] ) {

  int L, H, i, j ; 
  
  printf( "Enter the length of the box\n" ) ;
  scanf( "%d" , &L ) ;
  printf( "Enter the height of the box\n" ) ;
  scanf( "%d" , &H ) ;
  for( i = 0 ; i < L ; i++ )             /*print first line*/
    printf( "*" ) ;
  putchar( '\n' ) ;
  for( i = 0 ; i < ( H - 2 ) ; i++ ) {   /*print inner line with * on ends*/
    printf( "*" ) ;
    for( j = 0 ; j < ( L - 2 ) ; j++ ) 
      printf( " " ) ;
    printf( "*\n" ) ;
  }
  for( i = 0 ; i < L ; i++ )             /*print last line*/
    printf( "*" ) ;
  putchar( '\n' ) ;
  return 0 ;
}
