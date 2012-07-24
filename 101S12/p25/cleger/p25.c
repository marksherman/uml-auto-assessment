/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Unfilled Box                      */
/*                                              */
/*     Time to Completion: 40 mins              */
/*                                              */
/************************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int h;
  
  int w;
  
  int i;
  
  int j;
  
  printf( "Enter the height of the box:" );
  
  scanf( "%d", &h );
  
  printf( "Enter the width of the box:" );
  
  scanf( "%d", &w );


  for( i=0 ; i<w ; i++ ) {
    
    putchar( '*' );
  }

  for( i=0 ; i < (h-2) ; i++ ) {
    
    putchar( '\n' );
    
    for( j=0 ; j<w ; j++ ) {
      
      if( (j == 0) || (j == (w-1) ) ) {
	
	putchar( '*' );
	
      } else {
	
	putchar( ' ' );
      }
    }
  }
  
  putchar( '\n' );
  
  for( i=0 ; i<w ; i++ ) {
    
    putchar('*');
    
  }
  
  putchar( '\n' );

  
  return 0;
}
