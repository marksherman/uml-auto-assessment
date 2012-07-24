/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 26: Reverse                     */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
  int arrays[10];
  int i;

  printf( "enter 10 digits: \n" );
  
  for( i = 0; i <=  9; i++ ){
    scanf( "%d", &arrays[i] );
  }
  for( i = 9; i >= 0; i-- ){
    
    printf( "%d ", arrays[i] );
  }
  
  printf("\n");

  return 0;
}
