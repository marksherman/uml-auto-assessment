/********************************************************************************/
/*                                                                              */
/*  Mike Begonis                                                                */
/*  Program p23                                                                 */
/*                                                                              */
/*  This program reads a text file using fgetc and prints out an exact copy     */
/*  of the text except that all lower case letters are converted to upercase.   */
/*                                                                              */
/*  Approx Completion Time: 10 minutes                                          */
/********************************************************************************/


#include <stdio.h>
#include <ctype.h>

int main(int argc, char* argv[]){

  int x;
  FILE *fin;
  
  fin = fopen("testdata23","r");
  
  while ((x=toupper(fgetc(fin))) !=EOF){
    putchar(x);
  }
  fclose(fin);

  return 0;
}
