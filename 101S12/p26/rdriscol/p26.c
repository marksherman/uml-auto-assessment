/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: One Dimensional Array  */
/*                                 */
/* Approx Completion Time: 30 min  */
/***********************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]){

  int i;
  int number[15];
  FILE *fin;
  fin=fopen("testdata26","r");
  
  printf( "The numbers stored in testdata26 will be printed out in reverse order\n");
  
  for( i = 0; i < 15; i++){
    fscanf(fin,"%d",&number[i]);
  }
    for( i = 14; i >= 0; i--){
      printf( "The numbers are:%d\n", number[i]);
    }
  
    fclose( fin );
    return 0;
}
