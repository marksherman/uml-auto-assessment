/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 22: sum of a bunch                      */
/*  Aproximate Completion time: 15 Minutes          */
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
    int sum = 0;
    
    printf( "The numbers in the file add up to:\n" );

    fin = fopen( "testdata22" , "r" );
    
    while( fscanf( fin , "%d" , &x ) > 0 ) {
      sum += x ;
    }
    
    printf ("%d\n" , sum);
    
    fclose( fin );
    
    return 0;
}
