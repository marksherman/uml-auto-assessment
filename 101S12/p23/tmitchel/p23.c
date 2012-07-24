/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 23: fgetc and toupper                   */
/*  Aproximate Completion time: 11 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include<ctype.h>

int main( int argc, char *argv[] )
{

  int file;
  FILE* fin;
  
  fin = fopen ( "testdata23" , "r" );

  while ((  file = fgetc( fin )) !=EOF ){
    putchar( toupper (file) );
  }

  return 0;

}
