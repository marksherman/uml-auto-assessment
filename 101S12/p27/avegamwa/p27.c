
/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p27:Reverse Command Line       */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[])
{

  int Array[10];
  int i;

  printf( "Please enter 10 integers:\n" );

  for( i=0; i<10; i++ ){
    scanf( "%d", &Array[i] );
  }
  for( i=9; i>=0; i--){
    
    printf( "%d ", Array[i] );
    
  }

 printf( "\n" );
 return 0;

 
}
