/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 10 Sum of Twenty                                                     */
/*                                                                              */
/* Approximate Completion Time: 5 min                                           */
/********************************************************************************/

#include <stdio.h>

int main(){
  int i, currentn;
  int sum = 0;
  FILE* fin;
  fin = fopen("testdata10", "r");
  for(i = 0; i < 20; i++){
    fscanf(fin, "%d", &currentn);
    printf("%d\n", sum);
    sum = sum + currentn;
  }
  fclose(fin);
  printf("Sum = %d\n", sum);
  return 0;
}
