/********************************/
/* Prorammer: David Hoyt        */
/* Program: fscanf              */
/* Completion Time: 15min       */

#include <stdio.h>

int main(){
  
  int x;
 
  FILE* tst4;

   tst4 = fopen( "testdata4","r" );
      
   fscanf( tst4, "%d", &x );

   fclose( tst4 );

printf( "%d\n", x );

return 0;

}
