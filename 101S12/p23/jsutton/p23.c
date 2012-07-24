/*******************************************/
/* Programmer: Joanna Sutton               */
/*                                         */
/* Assignment: fgetc and toupper           */
/*                                         */
/* Approximate Completion Time:10 minutes  */
/*******************************************/

#include <stdio.h>
#include <ctype.h>

int main (int argc, char*argv[]){
  FILE *characters;
  char i;

  characters=fopen("testdata23","r");

  while((i=fgetc(characters))!=EOF){
  i=toupper(i);
  printf("%c",i);
  }
  
  fclose(characters);

  return 0;

}
