/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: fgetc and toupper                                                */
/*                                                                           */
/* Approximate completion time: 40 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
#include <ctype.h>
int main( int argc, char *argv[] )
{
  int x;
  FILE *fin;
  fin = fopen("testdata23", "r");
  /* opens the testdata23 file in order to read characters */
  while ((x = fgetc(fin)) != EOF){
  /* runs the loop until EOF is reached within testdata23 */
    x = toupper(x);
    /* converts lowercase letters to uppercase */
    putchar(x);
    /* writes the character to standard output */
  }
  fclose(fin);
  return 0 ;
}
