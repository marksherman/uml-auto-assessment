/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 13: Argc?                               */
/*  Aproximate Completion time: 11 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] )
{
  int q;

  printf( "%d\n" , argc );
  for ( q=0 ; q<argc ; q++ );
  return 0 ;
}

