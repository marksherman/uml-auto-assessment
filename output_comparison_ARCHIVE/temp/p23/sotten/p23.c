/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 23: fgetc and toupper   */
/*                                */
/*Approx. Completion Time: 25mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <ctype.h> /* library for toupper */

int main(int argc, char*argv[]){

  int sam;

  FILE*blah;

  blah=fopen("testdata23","r"); /*opens and reads contents of file testdata23*/

  while((sam=fgetc(blah))!=EOF){

    putchar(toupper(sam)); /* includes toupper into stand output */
 
  }
  
  fclose(blah);

return 0;
}

