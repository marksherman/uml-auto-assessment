/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 22: Sum of a Bunch                                             */
/*                                                                        */
/* Approximate Completion Time: 20 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
  int x = 0,y;

  fin = fopen("testdata22", "r");

  while(fscanf( fin, "%d", &y) != EOF){
    x = x + y;
  }

  printf("\n%d is the sum of all the numbers that was in the file. \n\n", x);

  fclose(fin);

  return 0 ;

}
