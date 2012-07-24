/*********************************************************/
/* Helen Chan                                            */
/* Assignment p21.c                                      */
/* Due March 28, 2012                                    */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include <stdio.h>

int main (int argc, char* argv[ ] )
{
  

  int a;
  FILE *fin;
  
  fin = fopen("testdata21","r");
  
  while(fscanf (fin, "%d", &a)!=EOF)
    printf ("%d",a);
  
 
 return 0 ;
  
}
