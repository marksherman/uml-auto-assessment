
/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 28: Digit Sum                           */
/*  Aproximate Completion time: 25 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include <stdio.h>
#include <stdlib.h>
#include <math.h>


int sum( int number )
{
  
  if( number<10 ) return number;

  return sum(number/10) + number % 10;

}


int main( int argc, char *argv[] )
{
    
    FILE *fin;
    int x = 0;
    int array[100];
    int z = 0;
    
    fin = fopen( argv[1] , "r" );

    printf ( "The integers in the file add up to these numbers:\n" );

    while ( fscanf( fin , "%d" , &array[x] ) != EOF ) {
	z = sum( array[x] );
	printf( "%d = %d\n" , array[x] , z );
    }
   
    return 0;
    
    fclose( fin );

}
