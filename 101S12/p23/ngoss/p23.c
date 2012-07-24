/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 23: fgetc and toupper                            */
/*                                                          */
/* Approximate completion time: 15 minutes                  */
/************************************************************/


#include <stdio.h>
#include <ctype.h>


int main(int argc, char* argv[])
{
  FILE* fin;
  int val;

  fin = fopen("testdata23","r");

  val = fgetc(fin);

  while(val!=EOF)
  {
    printf("%c", toupper(val));
    val = fgetc(fin);
  }

  fclose(fin);

  return 0;
}
