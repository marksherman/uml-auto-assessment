/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 22: Sum of a bunch                                     */
/* Approx Completion Time: 15 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
   
  int x;
  int sum = 0;
  FILE *fin;

  fin= fopen( "testdata22" , "r" );
  
  while ( fscanf( fin , "%d" , &x ) !=EOF){
    sum = sum + x;

  }
  printf ("The sum of the numbers in testdata22 is %d\n", sum );
  fclose( fin );
   
  return 0;
}
