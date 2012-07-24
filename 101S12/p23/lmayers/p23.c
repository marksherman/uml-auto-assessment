/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: fgetsc and toupper                                           */
/*                                                                       */
/* Approximate completion time: 30 minutes                               */
/*************************************************************************/
#include <stdio.h>
#include <ctype.h>

int main (int argc , char *argv[]) {

  int x;
  FILE *fin;

  fin = fopen("testdata23","r");

  while((x = fgetc(fin))!=EOF){
    x = toupper(x);
    putchar(x);
  }
  return 0;
}
