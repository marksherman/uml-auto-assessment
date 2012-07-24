/****************************************************/
/* Programmer: Kyle White                           */
/* Program 2: The scanf Function                    */
/* Approximate completion time: 20 minutes          */

#include <stdio.h>

int  main ()

{
  int x;

  printf( "Please enter a number:\n" );

  scanf ( "%d", &x);

  printf ( "You entered %d!\n", x );

  return 0;
}
