/***********************************************/
/*  Programmer: Zachary Durkee                 */
/*                                             */
/*  Program 11: The abs Function               */
/*                                             */
/*  Approximate completion time: 10 minutes    */
/***********************************************/

#include <stdlib.h>

#include <stdio.h>

int main( int argc, char* argv[] )

{

 int x;

 int y;

 printf( "Enter a Number:\n" );

 scanf( "%d", &x );

 y=abs( x );

 printf( "Absolute Value of Number Entered:\n" );

 printf( "%d\n", y );

return 0;

}
