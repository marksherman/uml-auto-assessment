/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Unfilled Box           */
/*                                 */
/* Approx Completion Time: 30 min  */
/***********************************/

#include <stdio.h>

int main(int argc, char *argv[]){

  int a,b,i,j;
  printf( "Enter two numbers with a space between them:");
  scanf( "%d%d", &a,&b);
  for(j = 0; j < b; j++){
    for(i = 0; i < a; i++){
      if( (i >= 1 && i < a-1) && (j > 0 && j < b-1))  
      printf( " " );
      else 
	printf("*");
    }
    printf( "\n");
  }
  return 0;
}
