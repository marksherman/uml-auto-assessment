/**************************************************************/
/* Programmer: Jeremy Krugh                                   */
/*                                                            */
/* Program 10: Sum of Twenty                                  */
/*                                                            */
/* Approximate time of completion:                            */
/**************************************************************/

#include <stdio.h>

int main(){

  int x;
  FILE* fin;
  int sum;

  fin = fopen("testdata10","r");
  fscanf(fin,"%d",&x);
  fclose(fin);

  sum = 0;

  for(x=1; x<=20; x++)
  sum+=x;
  printf("%d\n",sum);

  return 0;
}
