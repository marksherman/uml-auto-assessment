/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p8:Asteriks                    */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>

int main(){

  int x, i;
  FILE *testdata8;

  testdata8 = fopen( "testdata8", "r" );
  fscanf( testdata8, "%d", &x );

  for( i = 0; i < x; i++ ){
    printf( "*" );
}
  
  printf( "\n" );
  fclose( testdata8 );

 return 0;

}

