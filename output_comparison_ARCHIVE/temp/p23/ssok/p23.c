/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 23: fgetc and toupper           */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

int main (int argc, char* argv[])
{
  int x;
  FILE *testdata23;

  testdata23 = fopen("testdata23", "r");

  while  ((x = fgetc(testdata23)) != EOF){
    putchar (toupper(x) );
  }
  fclose (testdata23);
  
  return 0;
}
