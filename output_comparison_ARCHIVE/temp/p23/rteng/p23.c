/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p23: fgetc and toupper                        */
/*                                                       */
/* Approximate completion time: 25 minutes               */
/*********************************************************/
#include <stdio.h>
#include <ctype.h>
/*Library ctype.h needed to use toupper function*/
int main(int arc, char* argv[])
{
  int testdata23;
  FILE *fin;
  fin = fopen("testdata23", "r");
  /*Opens testdata23 as a readable file*/
  while ((testdata23=fgetc (fin)) != EOF)
  /*Will get characters from file, as long as it's not end of file*/
  /*Will also convert all lowercase letters into capital letters*/
    printf("%c", toupper(testdata23));
  fclose(fin);
  printf("\n");
  return 0;
}

