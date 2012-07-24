/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 6: Equal to zero?                       */
/*  Aproximate Completion time: 11 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] )
{
  int h , l , i , j ;

  printf ( "I make asterisks boxes!\n\n" );

  printf ( "Enter the height of your box\n" );
  scanf ( "%d" , &h );

  printf ( "Enter the width of your box\n" );
  scanf ( "%d" , &l );

  printf ( "\n" );
  printf ( "*" );

  for( i = 0 ; i < h ; i++){
    for ( j = 0 ; j < l ; j++ ){
      printf ( "*" ); 
    }
    printf ( "\n*" );
  }

  return 0 ;
}
