/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 39 Recursive Persistence of a number                                 */
/*                                                                              */
/* Approximate Completion Time:  25 min                                         */
/********************************************************************************/

#include <stdio.h>

int num_in(int product);
int persist_set(int product, int* f_1_ptr, int* f_2_ptr, int persistence);
int product_gen(int product, int* f_1_ptr, int* f_2_ptr);

int main(int argc, char* argv[]){
  int product = 1;
  int factor_1 = 1;
  int* f_1_ptr = &factor_1;
  int factor_2 = 1;
  int* f_2_ptr = &factor_2;
  int persistence = 1;
  printf("Please enter positive integer terminated with EOF: ");
  product = num_in(product);
  printf("\n");                        /* correct here*/
  if(product == 0)
    persistence = 1;
  else if(product < 10)
    persistence = 0;
  else
    persistence = persist_set(product, f_1_ptr, f_2_ptr, persistence);
  printf("Persistence is equal to %d\n", persistence);
  return 0;
}

int num_in(int product){
  int input = 0;
  if( (input = getchar()) != EOF){
    input = input - 48;
    product = input * product * num_in(product);
  }
  return product;
}

int persist_set(int product, int* f_1_ptr, int* f_2_ptr, int persistence){
  if(product > 9){
    *f_1_ptr = product;              /* correct here*/
    product = 1;
    product = product_gen(product, f_1_ptr, f_2_ptr);
    persistence++;
    persistence = persist_set(product, f_1_ptr, f_2_ptr, persistence);
  }
  return persistence;
}

int product_gen(int product, int* f_1_ptr, int* f_2_ptr){
  if(*f_1_ptr > 0){
    *f_2_ptr = *f_1_ptr % 10;
    *f_1_ptr = *f_1_ptr / 10;
    product = product * (*f_2_ptr) *product_gen(product, f_1_ptr, f_2_ptr);
  }
  return product;
}
