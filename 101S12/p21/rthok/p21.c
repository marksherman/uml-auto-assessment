/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 21 : Scanf return what?                                        */
/*                                                                        */
/* Approximate Completion Time: 20 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
  int x;

  fin = fopen("testdata21", "r");

  printf("\nThese are the numbers in the file:\n\n");

  while( fscanf(fin,"%d", &x)!= EOF){ 
    printf("%d\n", x);
  }

  printf("\n");

  fclose(fin);

  return 0 ;

}
