/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 31:Inner Product of Two Vectors     */
/*                                            */
/*Approximate completion time: 30 minutes     */
/**********************************************/

#include <stdio.h>

float inner( float u[], float v[], int size);

int main (int argc, char*argv[])
{
	float A[8];
	float B[8];
	float sum;
	int size, i;

	size = 8;

	sum = 0;
	
	printf("Enter 8 values for vector A\n");
	for(i = 0; i < size; i ++)
		scanf("%f", &A[i]);
	printf("Enter 8 values for vector B\n");
	for(i = 0; i < size; i++)
		scanf("%f", &B[i]); 	

	sum = inner( A, B, size);

	printf("The inner product is %f\n", sum);
	
	return 0;
}

float inner( float u[], float v[], int size)
{
	float C[size];
	float sum;
	int i;
	sum = 0;

	for(i = 0; i < size; i++){
		C[i] = u[i] * v[i];
		sum = sum + C[i];
	}	
	return sum;
} 
