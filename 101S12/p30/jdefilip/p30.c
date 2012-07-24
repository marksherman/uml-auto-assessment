/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Simulating Call by Reference */ 
/* Approximate Time: 15 minutes        */ 
/***************************************/ 

#include <stdio.h>
void swap ( int* a, int* b ); 
int main ( int argc, char* argv[] ) 
{
  int x, y; 
  printf( "Hi! Please enter an integer x and an integer y.\n" );
  scanf( "%d %d", &x, &y ); 
  swap ( &x, &y ); 
  printf( "When the values are swapped, x is %d and y is %d.\n", x, y ); 
  return 0; 
}

void swap ( int* a, int* b )
{ 
  int temp; 
  temp = *a; 
  *a = *b; 
  *b = temp;  
} 
