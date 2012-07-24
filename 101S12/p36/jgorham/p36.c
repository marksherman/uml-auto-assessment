/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 36 Persistence of a number                                           */
/*                                                                              */
/* Approximate Completion Time:  25 min                                         */
/********************************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
  int input = 0;
  int product = 1;
  int factor_1 = 1;
  int factor_2 = 1;
  int persistence = 1;
  printf("Please enter positive integer terminated with EOF: ");
  while((input = getchar()) != EOF){
    input = input - 48;
    product = product * input;
  }
  printf("\n");
  if(product == 0)
    persistence = 1;
  else if(product < 10)
    persistence = 0;
  else{
    while(product > 9){
      factor_1 = product;
      product = 1;
      while(factor_1 > 0){
	factor_2 = factor_1 % 10;
	factor_1 = factor_1 / 10;
	product = product * factor_2;
	printf("%d\n", product);
      }
      persistence++;
    }
  }
  printf("Persistence is equal to %d\n", persistence);
  return 0;
}
