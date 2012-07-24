/***********************************************************************/
/* Programmer: Jeremy Krugh                                            */
/*                                                                     */
/* Program 38: Recursive Digit Sum                                     */
/*                                                                     */
/* Approximate completion time: 45 minutes                             */
/***********************************************************************/

#include <stdio.h>

int total(int z);

int main(int argc, char* argv[]){

  int x;
  FILE* fin;
  int y;

  fin = fopen(argv[1], "r");
  fscanf(fin, "%d", &x);

  y = total(x);

  printf("Digit sum: %d\n", y);

  fclose(fin);

  return 0;
}

int total(int z){

  if(z<10)
    return 1;
  else
    return z%10 + total(z/10);
}
