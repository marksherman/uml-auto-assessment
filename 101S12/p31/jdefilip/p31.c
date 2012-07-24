/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Inner Product of Two Vectors */ 
/* Approximate Time: 30 Minutes        */ 
/***************************************/ 

#include <stdio.h>
float inner( float u[8] , float v[8], int size ); 
int main ( int argc, char* argv[] ) {
  int productactual = 0;    
  int sizeactual = 8;  
  int j; /* index variable */ 
  float x[sizeactual];  
  float y[sizeactual];   
   
  printf( "Please enter eight values into Array 1. " ); 
  for ( j = 0; (j < sizeactual); j++ ) 
    scanf( "%f", &x[j] ); 
  printf( "Please enter eight values into Array 2. " ); 
  for ( j = 0; (j < sizeactual); j++ ) 
    scanf( "%f", &y[j] ); 
  productactual = inner( x, y, sizeactual ); 
  printf( "The inner product of two vectors is %d.\n\n", productactual ); 
  return 0; 
}

float inner( float u[] , float v[], int size ) {
  int i; 
  float product = 0; 
  for ( i = 0; i < size; i++ )
    product = product + (u[i] * v[i]); /* recursive function call */ 
  return product;
} 
 
