/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name :Inner Product of Two Vectors      */
/*                                                */
/*Approximate completion time: 30 minutes         */
/**************************************************/
#include <stdio.h> 
/* header file */

float inner (float u[], float v[], int size); /* inner function prototype*/ 

int main(int argc, char* argv[]) /* main function starts */ 
{
  float u[8]; /* declare first vector */
  float v[8]; /* declare second vector */
  int i; /*declare counter */
  /* promopt user to type value */ 
  printf ("Please enter 8 values for first vector: ");
  for (i = 0; i < 8; i++)
    scanf("%f",&u[i]);
  printf ("Please enter 8 values for second vector: ");
  for (i = 0; i < 8; i ++)
    scanf("%f", v+i);
  /* call function inner and print the result on the screen*/
  printf ("The product of two vectors is: %f\n", inner (u, v,8));
  return 0;
}
/* define inner function */
float inner (float u[], float v[], int size){
  float p; /* declare the product */
  int i; /* declare counter */
  /* the loop excutes size times */
  for ( i = 0; i < size; i++)
    p +=v[i]*u[i];
  /* return the result */ 
  return p;
}
