/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 24: Find the Average                                   */
/* Approx Completion Time: 10 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
   
  float x;
  float sum = 0;
  float average;
  FILE *fin;

  fin= fopen( "testdata24" , "r" );
  
  while ( fscanf( fin , "%f" , &x ) !=EOF){
    sum = sum + x;

  }
  average = sum / 4;
  printf ("The average of the numbers in testdata24 is %f\n", average );
  fclose( fin );
   
  return 0;
}
