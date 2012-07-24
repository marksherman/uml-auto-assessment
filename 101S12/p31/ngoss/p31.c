/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 031: Inner Product of Two Vectors                */
/*                                                          */
/* Approximate completion time: 015 minutes                 */
/************************************************************/


#include <stdio.h>

float inner(float u[], float v[], int size);

int main(int argc, char* argv[])
{
    int i;
    float arr1[8],arr2[8];

    printf("Input eight values for array 1:\n");
    for(i=0;i<8;i++)
    {
	scanf("%f", &arr1[i]);
    }

    printf("Input eight values for array 2:\n");
    for(i=0;i<8;i++)
    {
        scanf("%f", &arr2[i]);
    }
    
    printf("The inner product of the arrays is: %f\n", inner(arr1,arr2,8));

    return 0;
}


float inner(float u[], float v[], int size)
{
    int i;
    float sum = 0;

    for(i=0;i<size;i++)
    {
	sum += u[i] * v[i];
    }
    
    return sum;
}
