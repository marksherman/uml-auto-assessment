/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : fgetc and toupper	   */
/*Completion time: 10 mins	   */
/***********************************/
#include<stdio.h>
#include<ctype.h>

int main( int argc , char* argv[]){
  
  FILE* fin;
  char l;

  fin = fopen( "testdata23" , "r" );

  while ( ( l = fgetc( fin )) != EOF ){
    
    putchar( toupper(l) );
  }
  fclose(fin);
  putchar('\n');

  return 0;
}
