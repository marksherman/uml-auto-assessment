/************************************************/
/* Programmer: Kyle White                       */
/* Program  28: Digit Sum                       */
/* Approximate completion time: 25 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int digitsum(int a);
int main (int argc, char* argv [])

{

  int x,y=0;
  FILE *fin;

  fin = fopen (argv[1], "r");

  fscanf(fin, "%d", &x);

  printf("\nThe number whose digits are being added is: %d", x);

  y=digitsum(x);

  printf("\nThe sum of the digits of %d is: %d\n\n",x,y);

  fclose (fin);

  return 0;

}

int digitsum(int a){

  int x;

  if (a == 0){

    x = a;

  }

  else {

    x = (a%10) + digitsum(a/10);

  }  

  return x;

}

