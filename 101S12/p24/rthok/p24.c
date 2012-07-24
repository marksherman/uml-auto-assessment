/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 24 : Find the Average                                          */
/*                                                                        */
/* Approximate Completion Time: 30 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
  int x = 0, y;
  float count;

  fin = fopen("testdata24", "r");

  while(fscanf(fin, "%d", &y)!= EOF){
    x = y + x;
    count++;
  }

  x = (float) x;

  printf("\nThe average of all the number in the file is %.3f\n\n", x/count);

  return 0 ;

}
