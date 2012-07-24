/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 23: fgetc and toupper                 */
/* Approximate completion time: 20 mins          */
/*************************************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc, char *argv[]){
  FILE *fin;
  int x;
  fin = fopen ("testdata23", "r");
  while((x = fgetc(fin))!= EOF){
    x = toupper(x);
    putchar(x);
  }
  fclose(fin);
  putchar('\n');

  return 0;
}
