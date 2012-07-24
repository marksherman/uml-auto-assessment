/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p10:for loop sum               */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>
int main()
{

  int i, x;
  int sum = 0;
  FILE *testdata10;
  
  testdata10 = fopen( "testdata10", "r" );

  for( i = 0; i < 20; i++ ){
    fscanf( testdata10, "%d", &x );
    sum = sum + x;
    
  }
  
  printf( "%d", sum ); 
  printf( "\n" );
  fclose( testdata10 );
 
  return 0;

}
