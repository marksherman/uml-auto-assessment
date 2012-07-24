/*Programmer: SCott Sok*/

/*Program 4: the fscanf function*/

/*Approciamte completion time: 1 hour*/


#include <stdio.h>

int main(){

  int x;
  
  FILE *testdata4;
  
  testdata4 = fopen("testdata4", "r");

  fscanf( testdata4, "%d", &x);

  printf( "how many points did the patriots win by against the ravens: %d\n", x);
 
  fclose( testdata4 );

  return 0;

}
