
/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 24: Find the Average                     */
/*                                                  */
/* Approximate completion time: 10 minutes          */
/****************************************************/

#include <stdio.h>

int main( int argc, char* argv[] ){

  float x = 0;
  float y = 0;
  float avg = 0;

  FILE* fin;
  fin = fopen("testdata24", "r");

  while(fscanf(fin, "%f", &y) != EOF){
    x += y;
      }

  fclose(fin);
  
  avg = (x/4);

  printf("\nThe average is: %f\n", avg);

  return 0;

}
