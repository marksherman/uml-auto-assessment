/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 4: The fscanf Function                                 */
/* Approx Completion Time: 15 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main(){
   
  int x;
  FILE *fin;

  fin= fopen( "testdata4" , "r" );   
  fscanf( fin , " %d" , &x );
  printf ( "The number stored in the file testdata4 was %d\n" , x );
  fclose( fin );
   
  return 0;
}
