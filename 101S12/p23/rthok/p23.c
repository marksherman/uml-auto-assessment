/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 23 : fgetc and toupper                                         */
/*                                                                        */
/* Approximate Completion Time: 40 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>
#include <ctype.h>

int main( int argc, char *argv[] ) {

  FILE* fin;

  char x;
  
  fin= fopen("testdata23","r");

  printf("\nThe file reads:\n\n");

  while((x = fgetc(fin))!=EOF){
    x = toupper (x);
    printf("%c", x);
    }

  putchar('\n');

  return 0 ;

}
