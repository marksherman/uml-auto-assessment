
/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*                                                  */
/*  Program 21: scanf returns what?                 */
/*                                                  */
/*  Aproximate Completion time: 25 Minutes          */
/****************************************************/


#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main( int argc, char *argv[] )
{
    
    FILE *fin;
    int x = 0;

    fin = fopen( "testdata21" , "r" );

    printf ( "The numbers in the file are:\n" );

    while( fscanf( fin , "%d" , &x ) != EOF ){
      printf( "%d\n" , x );
    }
   
    return 0 ;

    fclose( fin );
}
