/****************************************************/
/* Programmer: Harrison Kelly                       */
/*                                                  */
/* Program 38: Recursive Digit Sum                  */
/*                                                  */
/* Approximate completion time: 60 minutes          */
/****************************************************/

#include <stdio.h>

int digitsum( int x );
int main( int argc, char* argv[] ){

  int n = 0;
  int y = 0;

  FILE* fin;
  fin = fopen(argv[1], "r");

  printf("\nThe sum of the digits of the numbers entered are: \n");
  while( fscanf(fin, "%d", &n) != EOF ){ /* Checks if there are numbers in the file. */
    y = digitsum(n); /* Call to digitsum and prints out the scanned number. */
    printf("%d",y);
    printf("\n");
  }

  fclose(fin);

  return 0;
}

int digitsum( int x ){

  if( x <= 0 ){ /* if the number is less than or equal to zero, the digit sum is 0. */
    return 0;
  }

  else{
    return ( ( x%10 ) + digitsum( x/10 ) ); /* Chops off the last digit of the number */
  }                                          /*  and adds it to the number divided by */
					    /*   ten and repeats.                     */
}


      
