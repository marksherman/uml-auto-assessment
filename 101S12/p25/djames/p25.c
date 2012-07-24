/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 25: Unfilled Box                    */
/*                                            */
/*Approximate completeion time: 30 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  int L, H, z, w;

  printf( "enter two positive integers\n" );

  scanf( "%d %d", &L, &H);

  putchar( '\n' );

  for( w=1; w<=H; w++){

    for( z=1; z<=L; z++ ){

      if( (z==1 || z==L) || (w==1 || w==H)) {

	  printf( "*" );

	} else{

	    printf( " " );
      }
    }
    putchar( '\n' );
    
  }

  putchar( '\n' );

  return 0;
}
