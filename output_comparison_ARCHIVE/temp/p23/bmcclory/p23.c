/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #23: fgetc and toupper                        */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>

int main(int argc, char* argv[]){
 
  FILE *read;
  
  char ch;

  read = fopen("testdata23", "r");

  while((ch=fgetc(read)) != EOF){
    ch = toupper(ch);
    putchar(ch); /* this file is named testdata23 */
  }
  
  printf("\n");

  fclose(read);

  return 0;
}
