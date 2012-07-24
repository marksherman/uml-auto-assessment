/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : reverse  		   */
/*Completion time: 10 min	   */
/***********************************/
#include<stdio.h>

int main( int argc , char* argv[]){

  int num[10];
  int i,y;

  printf( "enter ten integer then return\n" );

  for ( i = 0 ; scanf("%d", &num[i]) != EOF ; i++ ); 
  /*requires user to manually enter EOF */
  y = i;

  for ( i = y-1 ; i >= 0 ; i-- ){
    printf("%d", num[i]);
    putchar(' ');
  }
  putchar('\n');
  return 0;
}
