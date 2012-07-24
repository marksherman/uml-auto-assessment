/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 21: scanf returns what?                                */
/* Approx Completion Time: 15 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
   
   int x;
  FILE *fin;

  fin= fopen( "testdata21" , "r" );
  
  while ( fscanf( fin , "%d" , &x ) !=EOF){
  printf ( "%d\n" , x );
  }  
  fclose( fin );
   
  return 0;
}
