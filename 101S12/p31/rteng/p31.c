/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program 31: Inner Product of Two Vectors              */
/*                                                       */
/* Approximate completion time: 40 minutes               */
/*********************************************************/
#include <stdio.h>
float inner(float u[], float v[], int size);
int main(int argc, char* argv[])
{
  int i, j, locationspots;
  printf("How many values will each array hold? Enter this integer value and hit Enter: ");
  scanf("%d", &locationspots);
  float vector1[i], vector2[j], innerproduct;
  printf("Enter the values for the first vector (hit Enter after each value): \n");
  for(i = 0; i < locationspots; i++)
    scanf("%f", &vector1[i]);
  printf("Enter the values for the second vector (hit Enter after each value): \n");
  for(j = 0; j < locationspots; j++)
    scanf("%f", &vector1[j]); 
  innerproduct = inner(vector1, vector2, locationspots);
  printf("The inner product of the two vectors is %f.\n", innerproduct);
  return 0;
}
float inner(float u[], float v[], int size)
{
  int k;
  float innerproduct = 0;
  for(k = 0; k < size; k++)
    {
    innerproduct = innerproduct + u[k] * v[k];
    }
  return innerproduct;
}
