/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 42:Malloc up Space for a two        */
/*           dimensional array                */
/*Approximate completion time: 40 minutes     */
/**********************************************/

#include <stdio.h>
#include <stdlib.h>

int main (int argc, char*argv[])
{
	int row, col, i, j, sum, r, c;
	sum = 0;
	int* array;

	printf("Enter the row length and column length\n");
	scanf("%d%d", &row, &col);

	array = malloc( row * col * sizeof(int));

	printf("Enter the values for the array\n");
	for(i = 0; i < row; i++){
		for(j = 0; j < col; j++)
			scanf("%d", &array[i * col + j]);
	}
	
	printf("Which row (0 to %d) would you like summed?\n", row - 1);
	scanf("%d", &r);
	for(i = 0; i < col; i++){
		sum = sum + array[r * col + i];
	}	
	printf("sum is %d\n", sum);
	
	sum = 0;

	printf("Which column (0 to %d) would you like summed?\n", col - 1);
	scanf("%d", &c);
	for(j = 0; j < row; j++){
		sum = sum + array[j * col + c];
	}
	printf("sum is %d\n", sum);

	sum = 0;

	for(i = 0; i < row; i++){
		for(j = 0; j < col; j++)
			sum = sum + array[i * col + j];
	}
	printf("Total sum is %d\n", sum);
	free (array);	
	return 0;
}

