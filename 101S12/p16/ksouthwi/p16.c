/***********************************************/
/* Programmer: Kevin Southwick                 */
/*                                             */
/* Program 16: Count Characters                */
/*                                             */
/* Approximate completion time: 35  minutes    */
/***********************************************/

#include <stdio.h>

int main ( int argc , char* argv[] ) {

  int c, x = 0 ;

  printf( "Type characters, end with ctrl+d twice.\n" );

  while( (c = getchar ()) != EOF ){

    x = x + 1 ;
    
 }

  printf ( " \n %d \n " , x ); 

  return 0;

}
