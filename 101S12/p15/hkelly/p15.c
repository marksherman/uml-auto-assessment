/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 15: Solid Box of Asterisks               */
/*                                                  */
/* Approximate completion time: 40 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  int l, h;
  int x, y;

  printf("\nEnter a Height:\n");
  scanf("%d", &h);

  printf("\nEnter a Length:\n");
  scanf("%d", &l);
  
  for( x = 0; x < h; x++){
    for( y = 0; y < l; y++){
      printf( "*" );
    }
    printf( "\n" );
  }
  printf( "\n" );
  return 0;
}
