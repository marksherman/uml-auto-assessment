/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Find the Average             */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/

#include <stdio.h>
int main ( int argc, char* argv[] ) 
{
  float a, b, c, d; 
  a = b = c = d = 0; /* clear variables of junk data */ 
  float average = 0; 
  FILE* fin; 
  fin = fopen( "testdata24", "r" ); 
  fscanf( fin, "%f%f%f%f", &a, &b, &c, &d ); /* although text data is composed of integer values, for calculation accuracy convert to float */  
  average = ( a + b + c + d ) / 4; 
  printf( "The average is %f\n", average ); 
  fclose ( fin ); 
  return 0; 
}
