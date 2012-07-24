/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 042: Malloc up Space for a Two-Dimensional Array */
/*                                                          */
/* Approximate completion time: 015 minutes                 */
/************************************************************/

#include <stdlib.h>
#include <stdio.h>

int main(int argc, char* argv[])
{
    int sum = 0, j, i, r, c, row, col;
    int* arrayofpointers_pointer;

    printf("Please input number of rows: ");
    scanf("%d", &r);
    printf("Please input number of columns: ");
    scanf("%d", &c);
    
    arrayofpointers_pointer = (int*) malloc (r * c * sizeof(int));
    
    printf("Please enter %d integers: ", r * c);

    for(i = 0; i < r; i++)
	for( j = 0; j < c; j++)
	    scanf("%d", &arrayofpointers_pointer[i*c + j]);

    printf("Choose a row to be summed: ");
    scanf("%d", &row);
    
    for(i = 0; i < r; i++)
	sum += arrayofpointers_pointer[row*c + i];

    printf("The sum is %d.\n", sum);

    printf("Choose a column to be summed: ");
    scanf("%d", &col);

    sum = 0;

    for(i = 0; i < c; i++)
	sum += arrayofpointers_pointer[i*c + col];

    printf("The sum is %d.\n", sum);

    sum = 0;

    for(i = 0; i < r; i++)
	for(j = 0; j < c; j++)
	    sum += arrayofpointers_pointer[i*c + j];

    printf("The sum of the entire array is: %d\n", sum);

    free(arrayofpointers_pointer);

    return 0;
}
