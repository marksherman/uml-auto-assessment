/**************************************************************************/
/* Programmer: Jeremy Krugh                                               */
/*                                                                        */
/* Program 28: Digit Sum                                                  */
/*                                                                        */
/* Approximate completion time: Unable to find and fix segmantaiton fault */
/**************************************************************************/

#include <stdio.h>

int total(int z);

int main(int argc, char* argv[]){

  int x;
  FILE* fin;
  int y;

  fin = fopen(argv[1],"r");
  fscanf(fin,"%d",&x);

  y = total(x);

  printf("Digit sum: %d", y);

  fclose(fin);

  return 0;
}

int total(int z){

  int k;
  int sum;

  k = 1;
  sum = 0;

  while(z != 0)
    {
      k = z%10;
      sum = sum + k;
      k = z/10;
      z=k;

    }

      return sum;
}    

/* I tried for hours to write this without segmentation faults, but could not determine where I am getting one. I fixed all errors and warnings but then once I ran it, I got the segmantation fault. */
