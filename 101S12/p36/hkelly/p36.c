/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 36: Persistence of a Number              */
/*                                                  */
/* Approximate completion time: 60 minutes          */
/****************************************************/

#include <stdio.h>

int persistence( int x );
int main( int argc, char* argv[] ){

  int n = 0;
  int y = 0;

  while( n != EOF){ /* Scans in new numbers unless EOF is entered */
    printf("\nEnter a number to check the persistence:\n");
    scanf("%d", &n);
    y = persistence(n); /* Passes entered number to function */
    printf("\nThe persistence of the number is: %d\n", y);
  }

  return 0;
}

int persistence( int x ){

  int total;
  int per = 0;
 
  while( x >= 10 ){ /* Runs while the number is more than one digit */
    total = 1;

    do{  /* Removes the last digit while the number entered is greater than zero */
      total *= (x % 10);
      x = x/10;
    }

    while ( x > 0 );{ /* While the # is greater than zero it replaces the total*/
      x = total;       /*  with the number entered divided by 10, to change */
      per++;          /*   to a lowered digit */
    }
  }

  return per;
}

