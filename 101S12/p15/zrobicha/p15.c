/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Solid Box of Asterisks                                       */
/*                                                                           */
/* Approximate Completion Time : 10 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int main( int argv , char* argc[]  ) {

  int L , H ,x , y ;

  printf( "Enter the length of the box :\n" ) ;
  scanf( "%d" , &L ) ;
  printf( "Enter the hight of the box :\n" ) ;
  scanf( "%d" , &H ) ;
  for( x=0 ; x<H ; x++ ) { 
      for ( y=0 ; y<L ; y++ ) {
	printf( "*" ) ; 
      } 
      putchar( '\n' ) ;
  }
  putchar('\n') ;
  return 0 ;
}
