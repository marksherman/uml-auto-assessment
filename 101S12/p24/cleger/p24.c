/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Find the Average                  */
/*                                              */
/*     Time to Completion: 10 min               */
/*                                              */
/************************************************/

#include<stdio.h>

int main( int argc, char *argv[]) {

  FILE *fin;

  int i;

  float num;

  float sum = 0;
  
  fin = fopen( "testdata24", "r" );

  for(i=0 ; i<4 ; i++ ) {

    fscanf( fin, "%f", &num );

    sum = sum + num;

  }

  printf( "The Average is:%f\n", sum / 4 );

  fclose( fin );

  return 0;

}
