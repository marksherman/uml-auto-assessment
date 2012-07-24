/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p9:for loop                    */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>
int main(){

  int i, a, b, c, d, e;
  FILE *testdata9;
  
  testdata9 = fopen( "testdata9", "r" );
  fscanf( testdata9, "%d%d%d%d%d", &a, &b, &c, &d, &e );
  
  for( i = 0; i < 1; i++ ){
    printf( "%d%d%d%d%d\n", a, b, c, d, e );
   
  }

  fclose( testdata9 );
  return 0;

}
