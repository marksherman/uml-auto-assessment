/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 6: Simulating Call by Reference         */
/*  Aproximate Completion time: 10 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

void swap( int *a , int *b ) ;

int main( int argc, char *argv[] )
{

  int x ;
  int y ;


  printf( "Enter two numbers and I'll swap them!\n" ) ;
  scanf( "%d%d" , &x , &y ) ;
  
  printf( "Your numbers are:\n x = %d\n y = %d\n" , x , y ) ;

  swap( &x , &y ) ;
  
  printf ( "Numbers swapped:\n x is now = %d\n y is now = %d\n" , x , y ) ;

  return 0 ; 
 
}

void swap ( int *a , int *b )
{

  int change ;

  change = *a ;
  *a = *b ;
  *b = change ;

  return ;

}
