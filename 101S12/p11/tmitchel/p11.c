/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 11: The abs function                    */
/*  Aproximate Completion time: 18 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include<stdlib.h>

int main( int argc, char *argv[] )
{
  int x;

  printf ( "Enter a number to get the absolute value\n" );
  scanf ( "%d" , &x );
  printf ( "%d\n" , abs (x) );
  
  return 0;
}
