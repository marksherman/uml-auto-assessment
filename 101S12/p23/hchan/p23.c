/*********************************************************/
/* Helen Chan                                            */
/* Assignment p23.c                                      */
/* Due March 28, 2012                                    */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include <stdio.h>
#include <ctype.h>

int main (int agrc, char *agrv[ ])
  
{
  

  char a;
  FILE *fin;
  
  fin = fopen( "testdata23", "r" );
  
  
  while( (a = fgetc(fin))!= EOF);    
    putchar(toupper(a));

    printf("%c", a);
    
    
    fclose(fin);
    
    return 0;
    
}
