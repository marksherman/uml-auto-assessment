                                                              
/***********************************/
/*Programmer: John Cavalieri       */
/* Program :  sum of a bunch       */
/*Completion time: 10 mins         */
/***********************************/

#include<stdio.h>

int main( int argc , char* argv[]){

  FILE* fin;
  int x;

  fin = fopen ( "testdata22", "r");

  while (  fscanf ( fin , "%d", &x) != EOF ){
    x = x + x;
  }  

  printf( "%d\n" , x);
  fclose( fin );
  return 0;
}


