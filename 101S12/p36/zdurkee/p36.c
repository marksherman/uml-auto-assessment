/***********************************************************/
/*  Programmer: Zachary Durkee                             */
/*                                                         */
/*  Program 36: Persistence of a Number                    */
/*                                                         */
/*  Approximate completion time: 3 hours                   */
/***********************************************************/

#include <stdio.h>

int pers( int integer );

int main( int argc, char *argv[] ){

  int num, answ;

  printf( "Enter a number: \n" );
  
  while ( scanf( "%d", &num) != EOF ){

    answ = persistence( num );

    printf( "The persistence of %d is: %d\n", num, answ );
  
  }
  
  return 0;

}

int persistence( int integer ){

  int i, product = 1, digit;

  for( i=0; integer > 9; i++ ){

    for( product = 1; integer != 0; ){
    
      digit = integer % 10;

      product = product * digit;

      integer = integer / 10;
    
    }

    integer = product;

  }

  return i;

}
