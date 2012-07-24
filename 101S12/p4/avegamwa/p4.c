/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p4: fscanf                     */
/*                                        */
/* Approximate completion time:15 minutes */
/******************************************/

#include <stdio.h>

int main(){

  int x;
  FILE *testdata4;
  
  testdata4 = fopen( "testdata4", "r" );
    fscanf( testdata4, "%d", &x );
    printf( "The number in this file is: %d\n", x);
    fclose( testdata4 );

return 0;

}
