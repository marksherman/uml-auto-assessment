/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : fgetc and toupper                */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/

#include <stdio.h>
#include <ctype.h> /* for toupper function */

int main (int agrc, char *agrv[])
{
  char i;
  FILE* fin;
  
  fin = fopen ( "testdata23", "r");
  /* read character from file until EOF is reached  */
  while ( (i=fgetc (fin))!= EOF)
    
    putchar (toupper(i));
  putchar ('\n');
  fclose (fin); /*closes the file */

  return 0;
}
