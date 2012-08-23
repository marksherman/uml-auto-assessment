/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Digit Sum                         */
/*                                              */
/*     Time to Completion: 1 Hour               */
/*                                              */
/************************************************/

#include<stdio.h>

int sum( int input );

int main( int argc, char *argv[] ) {

  FILE *fin;

  int num;

  int sumnum;

  /* fin = fopen( argv[1], "r" ); */ 
  fin = fopen( "testdata28", "r"); 

  while( fscanf( fin, "%d", &num ) != EOF ) {

  printf( "Integer Read: %d\n", num );

  sumnum = sum( num );

  printf( "The sum of digits is: %d\n\n", sumnum );

  }

  fclose( fin );




return 0;
    
}


int sum( int input ) {

  int mod;

  int digitsum = 0;

  while( input != 0 ) {

    mod = input % 10;

    digitsum += mod;
  
    input -= ( input % 10 );

    input /= 10;
    

  }

    return digitsum;
}
