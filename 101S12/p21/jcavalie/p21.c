/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  scanf returns what   */
/*Completion time: 5 mins	   */
/***********************************/

#include<stdio.h>

int main( int argc , char* argv[]){

  FILE* fin;
  int x;

  fin = fopen( "testdata21", "r");

  while (  fscanf ( fin , "%d", &x) != EOF )
    printf ( " %d", x );

  putchar('\n');
  fclose( fin );
  return 0;
}
