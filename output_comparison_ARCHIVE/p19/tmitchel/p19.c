/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 13: Argv                                */
/*  Aproximate Completion time: 17 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>

int main( int argc, char *argv[] )
{
  int q;

  printf ( "Alright, I'll print out that stuff you just typed!\n" );

  for ( q=0 ; q<argc ; q++ )
    printf ( "%s\n" , argv[q] );

  return 0;

}


