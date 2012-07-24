/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 27: Reverse                             */
/*  Aproximate Completion time: 11 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] )
{
  
  int i = 0;
  int array[10];

  printf( "Enter 10 numbers and I'll reverse them!\n" );
  
  for ( i=0 ; i<10 ; i++ ) {
    scanf( "%d" ,&array[i] );
    } 

  printf( "The numbers in reverse are as followed:\n" );

  for ( i = 9 ; i >= 0 ; i-- ) {
    printf( "%d\n" , array[i] );
  } 

  return 0;

}
