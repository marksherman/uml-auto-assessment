/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 04 : The fscanf Function                                       */
/*                                                                        */
/* Approximate Time: 20 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
 
  int x;

  fin = fopen("testdata4","r");

  fscanf(fin,"%d", &x);

  printf("\nThe value of the integer in file testdata4 was %d.\n\n",x);

  fclose (fin);
 
  return 0 ;

}
