/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : one dimensional array */
/*Completion time: 10 mins	   */
/***********************************/
#include<stdio.h>

int main( int argc , char* argv[]){
  
  FILE* fin;
  int num[15];
  int i,y;

  fin = fopen("testdata26", "r");

  for ( i = 0 ; (fscanf( fin, "%d" ,&num[i])) != EOF ; i++ );

  y = i;

  for ( i = y-1 ; i >= 0 ; i-- ){
    printf("%d", num[i]);
    putchar(' ');
  }
  fclose(fin);
  putchar('\n');
  return 0;
}
