/******************************************************/
/* Programmer:Joe LaMarca                             */
/* Program: fget and toupper                          */
/* Approximate time of completion:1 hour              */
/******************************************************/

#include <stdio.h>
#include <ctype.h>

int main(int argc, char*argv[]){

  int x;
  FILE* fin;
  fin=fopen("testdata23","r");

  while((x=fgetc(fin))!=EOF)
    putchar(toupper(x));

  fclose(fin);

  return 0;
}
