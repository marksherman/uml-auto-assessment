/**************************************************************************/
/*  Programmer: Zachary Durkee                                            */
/*                                                                        */
/*  Program 39: Persistence of a Number                                   */
/*                                                                        */
/*  Approximate completion time: 4 hour                                   */
/**************************************************************************/

#include <stdio.h>

int scanword();

int persistence( int integer, int strtcount );

int multiply( int integer, int product );

int main( int argc, char *argv[] ){

  scanword();

  return 0;

}

int scanword(){

  int num, answ;
  
  printf( "Enter a number:  \n" );

  if( scanf( "%d", &num) != EOF ){

    answ = persistence( num, 0 );

    printf( "The persistence of %d is: %d\n", num, answ );

    scanword();

  }

  return 0;

}

int persistence( int integer, int count ){

  int smallint;

  if( integer > 9 ){

    smallint = multiply( integer, 1 );
    
    count++;
    
    return persistence( smallint, count );

  }

  return count;

}


int multiply( int integer, int product ){

  int digit;

  digit = integer % 10;

  product = product * digit;

  integer = integer / 10;

  if( integer != 0 )
    
    return multiply( integer, product );

  else

    return product;

}
