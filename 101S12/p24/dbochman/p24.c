/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 24:   Find the Average                       */
/* Time:         15 minutes                             */
/********************************************************/
#include <stdio.h>
int main ( int argc, char *argv[] ) {
  int number, sum, count ;
  double average;
  FILE *fin = fopen ( "testdata24", "r" ) ;
  sum = 0;
  count = 0;
  while (fscanf (fin, "%d", &number) != EOF)
    {
      sum = sum + number;
      count++;
    }
  fclose (fin);
  average=(double)sum / count;
  printf("The average of the numbers is %.2f.\n",average);
  return(0);  
}
