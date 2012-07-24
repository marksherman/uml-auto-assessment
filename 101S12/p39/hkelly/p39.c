/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 39: Recursive Persistence                */
/*                                                  */
/* Approximate completion time: 120 minutes         */
/* (Most of the time was spent getting the          */
/*  Persistence function working since there was 3  */
/*  loops which I had to make recursive)            */
/****************************************************/

#include <stdio.h>

int per = 0; /* Sets initial value of counter to zero (global) */
int end_file_check( int n );
int persistence( int x, int per, int total );

int main( int argc, char* argv[] ){

  int n = 0;

  printf("\nEnter a number to check the persistence:\n");
  scanf("%d", &n); /* Initial scan to start checking for EOF */
  
  end_file_check( n );

  return 0;
}

int end_file_check( int n ){

  int y = 0;
  int total = 1;

  if( n != EOF && n >= 10 ){ /* Checks if n is EOF and greater than 10 */
    y = persistence( n, per, total); /* Calls persistence check with n */
    printf("\nThe persistence of the number is: %d\n", y);
    printf("\nEnter a number to check the persistence:\n");
    scanf("%d", &n); /* Rescans and recurses until EOF/under 10 */

    return end_file_check( n );
  }
  else if( n > 0 ){ /* If n is under 10, persistence is zero and recurse function */
    printf("\nThe persistence of the number is: 0\n");
    printf("\nEnter a number to check the persistence:\n");
    scanf("%d", &n); /* Rescans to check for EOF/<10 again */

    return end_file_check( n );
  }
  else{
    return 0;
  }
}

int persistence( int x, int per, int total ){
 
    total *= (x % 10);
    x = x/10;

    if ( x == 0 ){ /* If number is zero, add one to the counter and reset total */
      x = total;
      total = 1;
      per++;
      
      if ( x <= 9 ){ /* If the total is less than 10, return the counter */
	return per;
      }
      else{
	return persistence( x, per, total ); /* If it is not, restart function */
      }
    }

  else{
    return persistence( x, per, total );
  }
    }
