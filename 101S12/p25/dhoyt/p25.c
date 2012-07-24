/**************************/
/* Programmer: David Hoyt */
/* Program: Unfilled box  */
/* Time: 30min            */

#include <stdio.h>
#include <stdlib.h>

int main(){
  
  int le = 0, he = 0, i, j;

  printf( "Please enter the length and height of your box, in that order:" );
  
  scanf( "%d%d", &le, &he ); 

  for( i=0; i<le; i++ ){

    putchar( '*' );
 
  }

  putchar( '\n' );

  for( j=0; j<he-2; j++ ){
    
    for( i=0; i<le; i++ ){

      if( i==0 )

	putchar( '*' );

      else if( i==(le-1) )
	
	putchar( '*' );
	  
      else

	  putchar( ' ' );
      }

      putchar( '\n' );

  }


  for( i=0; i<le; i++ ){

    putchar( '*' );

  }

  putchar( '\n' );

  return 0;

  }
