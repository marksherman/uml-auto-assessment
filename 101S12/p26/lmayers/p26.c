/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: One Dimensional Array                                        */
/*                                                                       */
/* Approximate completion time: 20 minutes                               */
/*************************************************************************/
#include <stdio.h>

int main (int argc , char *argv[] ) {
  
  int i;
  FILE * fin;
  int A [15];

  fin = fopen ("testdata26","r");

  for (i = 0; i < 15 ; i++)
    fscanf (fin,"%d", &A[i]);

  for (i = 14; i >= 0; i--)
    printf("%d\n ", A[i]);

  fclose(fin);
  
  return 0;

}
