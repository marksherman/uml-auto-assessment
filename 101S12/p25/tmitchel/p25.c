/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 25: Unfilled box                        */
/*  Aproximate Completion time: 24 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] )
{

  int l = 0;
  int h = 0;
  int i = 0;
  int j = 0;

  printf( "I make asterisk boxes! Try me!\n" );

  printf( "Enter the Length of your box\n" );
  scanf( "%d" , &l );

  printf( "Enter the Height of your box\n" );
  scanf( "%d" , &h );

  if ((l&&h>= 1)&&(l&&h<=21)) {
    i = 0;

    while ( i<h ) {
      i++;
      j = 0;

      while ( j <= l ) {
	if ((( j > 1 )&&( j < l ))&&(( i > 1)&&( i < h))) {
	  printf ( " " );

	  }
	
	else if (( j!=0 )&&( i!=0 )) {
	  printf( "*" );
	
	}

    j++;

      }

      printf( "\n" );

    }

  }

    else
      printf( " Length and Height must be between 1 and 21. \n" );
  
  return 0;

}


	 
