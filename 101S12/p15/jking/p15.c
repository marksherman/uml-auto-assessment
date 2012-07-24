/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 15: Solid Box of Astericks                             */
/* Approx Completion Time: 10 minutes                             */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
  
  int l;
  int h;
  int i; 
  int x; 
  
  printf( "Enter length of the box: " );
  scanf( "%d", &l );
  printf( "Enter height of the box: " );
  scanf( "%d", &h );
  
  for( x=0 ; x<h ; x++ ){
    for( i=0 ; i<l ; i++ ){    
      printf( "*" );
}
    printf("\n"); 
}
  return 0;
}

