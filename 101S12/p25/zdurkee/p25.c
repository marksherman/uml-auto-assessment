/**********************************************/
/*  Programmer: Zachary Durkee                */
/*                                            */
/*  Program 25: Unfilled Box                  */
/*                                            */
/*  Approximate completion time: 1 hr         */
/**********************************************/


#include <stdio.h>

int main( int argc, char *argv[] ){

  int L, H, i, j, q;

  printf( "Enter the length and height dimensions of the block, respectively:  \n");

  scanf( "%d %d", &L, &H);

  if ( L<21 && H<21 ){

    for ( j=1; j<=H; j++ ){

      if ( j==1 || j==H ){
	
	for ( i=1; i<=L; i++ ){

	  printf( "*" );

	}

	printf( "\n" );

      }
      
      else {

	for ( q=1; q<=L; q++ ){

	  if ( q==1 || q==L ){

	    printf( "*" );

	  }

	  else{

	    printf( " " );

	  }
	
	}
	
	printf( "\n" );

      }

    }

  }

  else{

    printf( "One of the dimensions entered are over 21 \n" );

  }
  
  return 0;

}
