/**************************************************************************/
/* Programmer: Jeremy Krugh                                               */
/*                                                                        */
/* Program 28: Digit Sum                                                  */
/*                                                                        */
/* Approximate completion time: 30 minutes                                */
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

  printf("Digit sum: %d\n", y);

  fclose(fin);

  return 0;
}

int total(int z){

  int sum = 0;

  while(z > 0){
    sum += z%10;
    z = z/10;
  }
  return sum;
}
