/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 24: Find the Average                */
/*                                            */
/*Approximate completeion time: 15 minutes    */
/**********************************************/

#include <stdio.h>
int main(int argc, char* argv[]){  

  FILE* fin;

  int x[4];

  int i;

  float sum, avg;

  fin = fopen( "testdata24", "r" );

  for( i=0; fscanf( fin, "%d", &x[i] ) != EOF; i++ );

  sum = x[0] + x[1] + x[2] + x[3];

  avg = sum/4;

  printf( "%f\n", avg );

  return 0;
}
