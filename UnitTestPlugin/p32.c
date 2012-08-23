/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Inner Product of Two Vectors                                     */
/*                                                                           */
/* Approximate completion time: 1 hour 45 minutes                            */
/*****************************************************************************/

#include <stdio.h>
float inner (float u[], float v[], int size);
int main( int argc, char *argv[] )
{
  int i, j;
  int size = 8;
  float x[8], y[8], z;
  printf("Please enter the 8 floating point numbers of the first of the two vectors you wish to multiply:\n");
  /* prompts the user for the elements of the first vector */
  for (i=0; i<size; i++){
  /* runs loop once for every element in the array */
    scanf("%f", &x[i]);
    /* enters the data into the corresponding element */
  }
  printf("Please enter the 8 floating point values of the second of the two vectors you wish to multiply:\n");
  /* prompts the user for the elements of the second vector */
  for (j=0; j<size; j++){
  /* runs loop once for every element in the array */
    scanf("%f", &y[j]);
    /* enters the data into the corresponding element */
  }
  z = inner (x, y, 8);
  /* calls the inner function and stores the return value into the variable z */
  printf("The inner product of the two vectors is %f.\n", z);
  /* prints the inner product of the two vectors */
  return 0 ;
}

float inner(float u[], float v[], int size)
{
  int i;
  float product, sum;
  for(i=0; i<=(size-1); i++){
  /* runs the loop for each element */
    product = ((u[i]) * (v[i]));
    /* takes the product of the two elements */
    sum = sum + product;
    /* adds the previously calculated product to the cummulative sum with each iteration of the loop */
  }
  return sum ; 
  /* returns the cummulative sum of the products to the main function */
}
