/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 035: Passing a Two Dimensional Array             */
/*                                                          */
/* Approximate completion time: 010 minutes                 */
/************************************************************/


#define MATRIX_LENGTH 3
#include <stdio.h>


int sum(int matrix[][3], int length);

int main(int argc, char* argv[])
{
    int i, j, intmatrix[MATRIX_LENGTH][3];

    printf("Input nine integers: ");

    for(i = 0; i < 3; i++)
	for(j = 0; j < 3; j++)
	    scanf("%d", &intmatrix[i][j]);

    printf("The sum of the integers is: %d\n", sum(intmatrix, MATRIX_LENGTH));

    return 0;
}


int sum(int matrix[][3], int length)
{
    int i, j, sum = 0;

    for(i = 0; i < length; i++)
	for(j = 0; j < 3; j++)
	    sum += matrix[i][j];

    return sum;
}
