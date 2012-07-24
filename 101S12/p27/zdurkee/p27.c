/*************************************************************/
/*  Programmer: Zachary Durkee                               */
/*                                                           */
/*  Program 27: Reverse                                      */
/*                                                           */
/*  Approximate completion time: 15 minutes                  */
/*************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ){

  int array[10], i;

  printf( "Enter 10 integer numbers separated by spaces: \n" );

  for( i=1; i<=10; i++){

    scanf( "%d", &array[i] );

  }

  for( i=10; i>0; i-- ){

    printf( "%d", array[i] );

    printf( " " );
    
  }

  printf( "\n" );

  return 0;

}
