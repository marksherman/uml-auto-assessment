/************************************************/
/* Programmer: Kyle White                       */
/* Program  37: Digit Sum                       */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int digitsum(int a);
int main (int argc, char* argv [])

{

  int x,y=0;
  FILE *fin;

  fin = fopen (argv[1], "r");

  while ( fscanf(fin, "%d", &x) != EOF ){

    printf("\nThe number whose digits are being added is: %d", x);

    y=digitsum(x);

    printf("\nThe sum of the digits of %d is: %d\n\n",x,y);

  }

  fclose (fin);

  return 0;

}

int digitsum(int a){

  int sum=0;

  while ( a>0 ){

    sum+=a%10;
    
    a=a/10;
    
  }
  
  return sum;

}

