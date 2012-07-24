/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 24: Find the Average                  */
/* Approximate completion time: 25 mins          */
/*************************************************/

#include <stdio.h>

float av(int x[]);

int main( int argc, char *argv[]){
  int x[4], i;
  i = 0;
  FILE *fin;
  fin = fopen ("testdata24", "r");
  while( i < 4 ){ 
    fscanf(fin, "%d", &x[i]);
      i++;
      }
  printf("%f\n", av(x));
  fclose(fin);
  return 0;
}

float av(int x[])
{
  int sum;

  sum = x[0] + x[1] + x[2] + x[3];
  
  return ((float)sum)/4;
}
