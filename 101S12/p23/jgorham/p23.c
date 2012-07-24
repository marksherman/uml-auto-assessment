/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 23                                                                   */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int cin;
  FILE* fin = fopen("testdata23","r");
  while((cin = fgetc(fin)) != EOF){
    cin = int toupper(int cin);                /* Can't figure out warning here*/
    putchar(cin);
  }
  fclose(fin);
  return 0;
}
