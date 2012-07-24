
/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 8: One Dimensional Array                */
/*  Aproximate Completion time: 25 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc, char *argv[] )
{
    
    FILE *fin;
    int x = 0;
    int array[15];

    fin = fopen( "testdata26" , "r" );

    printf ( "The numbers in the file are:\n" );
    
    for( x=0 ; x<15 ; x++ ) {
	fscanf( fin , "%d" , &array[x] );
    }

    for( x=14 ; x>=0 ; x--){
      printf( "%d\n" , array[x] );
    }

    return 0;

    fclose( fin );

}
