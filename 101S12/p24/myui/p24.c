/**********************************************/
/* Programmer: Ming Yui Chung Jacky           */
/*                                            */
/* Program : Fine the Average                 */
/*                                            */
/* Approximate completion time: 10 minutes    */
/**********************************************/

#include<stdio.h>

int main( int argc, char *argv[] ) {

  int data, total;
  float average;
  FILE *scanfile;

  total = 0;
  average = 0;
  scanfile = fopen( "testdata24" , "r" );

  while ( fscanf( scanfile, "%d", &data ) != EOF ) {
    
    total = total + data;
  }
  
  average = (float)total / 4;

  printf( "The average is %f.\n", average );

  fclose( scanfile );

  return 0;
}
