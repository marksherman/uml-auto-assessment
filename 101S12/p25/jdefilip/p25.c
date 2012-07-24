/*****************************************/ 
/* Programmer: James W. DeFilippo        */  
/* Program 25: Unfilled Box              */ 
/* Approximate Time: 30 minutes          */ 
/*****************************************/ 


#include <stdio.h>
int main ( int argc, char* argv[] ) 
 
{
  int L; 
  int H; 
  printf( "Please enter length, then height, of the box." ); 
  scanf( "%d %d", &L, &H ); 
  while ( ( L > 21 || L < 0 ) && ( H > 21 || H < 0 ) ) { /* length and height must be positive integers which are less than 21 */ 
    printf ( "Please enter values between 1 and 21. \n" );
    scanf( "%d %d", &L, &H ); 
  }
  if ( ( L < 21 && L > 0 ) && ( H < 21 && H > 0 ) ) { 
      int i; /* the length index variable */ 
      int j; /* the height index variable */ 
      for ( j=1; j <= H; j++ ) { 
      	if ( j == 1 || j == H ) { /* test whether height is at top or bottom */ 
	  for ( i = 1; i <= L; i++ ) /* if at bottom, fill line completely with asterisks */ 
	    printf( "*" ); 
          i = 1; /* reset the length variable for next height */ 
        }
	  else {
	    for ( i = 1; i <= L; i++ ) {
	      if ( i == 1 || i == L ) /* test whether length is at beginning or end of open line */  
		printf( "*" ); 
	        else 
		  printf( " " );  
              }
	    i = 1; /* reset the length variable for next height */  
          }
	printf( "\n" );      
      }
  } 
 return 0; 
 }

  
