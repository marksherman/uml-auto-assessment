/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Recursive Digit Sum               */
/*                                              */
/*     Time to Completion: 20 mins               */
/*                                              */
/************************************************/

#include<stdio.h>

int digitsum( int input );

int main( int argc, char *argv[] ) {
  
  FILE *fin;
  int num;
  int sumnum;
  
  fin = fopen( argv[1], "r" );
  
  while( fscanf( fin, "%d", &num ) != EOF ) {
    
    printf( "Integer Read: %d\n", num );
    sumnum = digitsum( num );
    printf( "The sum of digits is: %d\n\n", sumnum );
  }
  fclose( fin );
  return 0;    
}

int digitsum( int input ) {
    int mod;
  
  if( input == 0 )
    return 0;
  else{
    mod = input % 10;
    input -= ( input % 10 );
    input /= 10;    
    return( mod + digitsum( input ) );
  }
}
