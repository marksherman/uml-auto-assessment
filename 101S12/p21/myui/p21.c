/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Scanf returns what?              */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int numbers;
  FILE *scanfile;

  numbers = 0;
  scanfile = fopen( "testdata21", "r" );
  
  while( fscanf( scanfile, "%d" , &numbers  ) != EOF ){
   
    printf( "%d ", numbers );
    
  }
  
  printf( "\n" );

  fclose( scanfile );

  return 0;

}
