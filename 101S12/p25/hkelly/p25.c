/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 25: Unfilled Box                         */
/*                                                  */
/* Approximate completion time: 10 minutes          */
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
      if( x == 0 || x == h-1 ){
	printf("*");
      }
      else if( y == 0 || y == l-1 ){
	printf("*");
      }
      else printf(" ");
    }
    printf( "\n" );
  }
  printf( "\n" );
  return 0;
}
