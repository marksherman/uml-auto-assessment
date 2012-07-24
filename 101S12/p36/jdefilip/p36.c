/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title:                              */ 
/* Approximate Time: 90 minutes        */ 
/***************************************/ 

#include <stdio.h>
int persistence ( int num ); 
int main ( int argc, char* argv[] ) 
{  
  int x; 
  while ( (scanf( "%d", &x )) != EOF ) {
    printf( "Persistence: %d\n", persistence ( x ) ); 
  }
  return 0; 
} 
  
int persistence ( int num )
{
  int product = 1; 
  int digit; 
  int count = 0; 
  if ( num > 9 ) 
    count = count + 1; 
  while ( num > 9 ) { 
    digit = num % 10; 
    num = num / 10; 
    product = product * digit; 
  } 
 product = product * num; 

 while ( product > 9 ) { 
    num = product;    
    product = 1; 
    while ( num > 9 ) { 
      digit = num % 10; 
      num = num / 10; 
      product = product * digit; 
    }
    product = product * num; 
    count = count + 1; 
  }  
 return count; 
}
   


