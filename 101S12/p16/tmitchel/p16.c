/****************************************************/
/*  Programmer: Thomas Mitchell                     */
/*  Program 16: Count Characters                    */
/*  Aproximate Completion time: 15 Minutes          */
/*                                                  */
/*                                                  */
/*                                                  */ 
/****************************************************/


#include<stdio.h>
#include <stdlib.h>

int main( int argc, char *argv[] )

{
  int x;
  int y; 
  
  printf ( "I count characters (including enter), try me! Type some stuff!\n" );

  while ( ( x = getchar( ) ) != EOF ){
      
    y++;

      printf ( "%d Characters \n"  , y );
  
  }

  return 0;

}
