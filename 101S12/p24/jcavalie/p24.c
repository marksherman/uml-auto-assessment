/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : Find the average	   */
/*Completion time: 5 mins	   */
/***********************************/
#include<stdio.h>

int main( int argc , char* argv[]){
  
  FILE* fin;
  float x,y;
  int i;
  
  fin = fopen( "testdata24", "r" );
  
  for ( i = 1 ; fscanf( fin, "%f" , &x ) != EOF ; i++ )
        x = x + x;

  fclose(fin);
  
  y = x/i;

  printf("the average is: %f\n", y);

  return 0;
}
