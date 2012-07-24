/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program:23 fgetc and toupper    */
/*                                 */
/* Approx Completion Time:30 min   */
/***********************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc, char* argv[]){

  char x;
  FILE *fin;
  fin = fopen( "testdata23","r");
  x = fgetc( fin );

  while(x != EOF){
    putchar( toupper(x));
    x = fgetc( fin );
  }
  
  fclose( fin );
  return 0;
}
