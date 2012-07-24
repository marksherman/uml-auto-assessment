/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: Reverse                */
/*                                 */
/* Approx Completion Time: 30 min  */
/***********************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x[10];
  int i;
  
  printf( "Enter any 10 numbers with a space between them:");
   
  for( i = 0; i < 10; i++){ 
    scanf("%d",&x[i]);
      }
  for ( i = 9; i >= 0; i--){
    printf( "The numbers in reverse order are:%d\n",x[i]);
  }
  return 0;
}
