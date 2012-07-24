
/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 38: Recursive Digit Sum                 */
/*  Aproximate Completion time: 10 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int sum ( int number ) ;

int main( int argc, char *argv[] ) {
    
    FILE *fin ;
    int num = 0 ;

    /* Opening file from command line which is = fin */
    
    fin = fopen( argv[1] , "r" ) ;

    /* Promting user */

    printf ( "The integers in the file add up to these numbers:\n" ) ;

    /* Reading the file until EOF, calling sum ( num ) within printf */

    while ( fscanf( fin , "%d" , &num ) != EOF ) {
	printf( "%d = %d\n" , num , sum ( num ) ) ;
    }
   
    fclose( fin ) ;

    return 0 ;
    
}

int sum ( int number ) {
  
  if ( number < 10 ) return number ; 

  return sum ( number / 10 ) + number % 10 ;

}
