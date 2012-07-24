/**********************************/
/* Author: James DeFilippo        */
/* Title : Solid Box of Asterisks */
/* Approximate Time: 20 minutes   */
/**********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
  int L, H; /* variables to store input values */ 
  int i, j; /* index vars for for loops */ 
   
  printf( "Hi! Please input values for length and height.\n" ); /* prompt for MEANINGFUL input */ 
  scanf( "%d %d", &L, &H); 
 
  if ( L <= 21 && H <= 21 ) /* set maximum values for L and H */  
    {
      for ( j=1; j<=H; j++ ) /* outer loop to start a line of asterisks */ 
	{
	  for ( i=1; i<=L; i++ ) /* inner loop to print a line of asterisks */ 
	  printf( "*" );
	  i = 1; /* reset i for next loop on a different line of asterisks */ 
	  printf( "\n" ); 
	}
    }
  else 
    printf("Please enter values that are less than 21.\n"); /* error message */ 
  return 0; 
}

