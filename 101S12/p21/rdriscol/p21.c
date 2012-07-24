/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program Scanf returns what?     */
/*                                 */
/* Approx Completion Time: 30 min  */
/***********************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int a;
  FILE* fin;
  fin = fopen( "testdata21", "r");
  
  while( fscanf (fin, "%d", &a) != EOF){
    printf( "The numbers stored in the file named testdata21 are:%d\n", a);
  }
    fclose( fin );
  
    return 0;
} 
