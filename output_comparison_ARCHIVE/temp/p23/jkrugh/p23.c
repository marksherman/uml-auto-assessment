/*****************************************************************/
/* Programmer: Jeremy Krugh                                      */
/*                                                               */
/* Program 23: fgetc and toupper                                 */
/*                                                               */
/* Approximate time of completion: 30 minutes                    */
/*****************************************************************/

#include <stdio.h>
#include <ctype.h>

int main(int argc, char*argv[]){

  int x;
  FILE *fin;

  fin = fopen("testdata23","r");
 
  while((x = fgetc(fin)) != EOF)
    putchar(toupper(x));

  fclose(fin);

  return 0;
}

