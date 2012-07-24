/*****************************************************************/
/* Programmer: Jeremy Krugh                                      */
/*                                                               */
/* Program 24: Find the Average                                  */
/*                                                               */
/* Approximate time of completion: 30 minutes                    */
/*****************************************************************/

#include <stdio.h>

int main (int argc, char* argv[]){

  float x;
  float sum;
  FILE* fin;

  fin = fopen("testdata24", "r");
 
  sum = 0;
  for(x = 0; fscanf(fin,"%f",&x) != EOF; x += 1)
      sum += x;

  printf("%f\n", (sum/4));

  fclose (fin);

  return 0;
}
