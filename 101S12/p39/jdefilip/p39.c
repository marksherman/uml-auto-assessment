/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Recursive Persistence        */ 
/* Approximate Time: 90 minutes        */ 
/***************************************/ 

#include <stdio.h>
int digit_multiply ( int phase ); 
int reduce ( int num, int i ); 
int read_in ( int x, int i ); 

int main ( int argc, char* argv[] ) { 
  int x = 0; 
  int i = 0;
  read_in ( x, i ); 
  return 0; 
}


int read_in ( int x, int i ) { 
  printf( "Please enter a positive integer.\n" ); 
  if ((scanf("%d", &x)) == EOF )
    return 0; 
  else {
    printf("Persistence of the number is %d.\n", reduce (x, i)); 
    return read_in ( x, i ); 
  }
} 
 
int reduce ( int num, int i ) { 
  if ( num <= 9 )
    return i; 
  else
    i++; /* if we get to this stage, we know that the number is a two-digit number in which case persistence is at least 1 */ 
    return reduce( digit_multiply( num ), i++ ); 
}  

int digit_multiply ( int phase ) { 
  if ( phase < 10 ) 
    return phase;  
  else 
     return (phase % 10) * digit_multiply ( phase / 10 );  
 } 
  






































