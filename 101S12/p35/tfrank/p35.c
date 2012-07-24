/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 35:Passing a two Dimensional Array  */
/*                                            */
/*Approximate completion time: 30 minutes     */
/**********************************************/

#include <stdio.h>

int sum(int array[3][3] );

int main (int argc, char*argv[])
{
	int x[3][3];
	int y, z;
	y = z = 0;

	printf("enter 9 values to be summed\n");

	for(y = 0; y < 3; y++){
		for(z = 0; z < 3; z++)	
			scanf("%d", &x[y][z]);
	}

	y = sum( x );

	printf("The sum is %d\n", y);

	return 0;
}

int sum (int x[3][3]){

	int i, j, sum;
	
	sum = 0;
	
	for(i = 0; i < 3; i++)
		for( j = 0; j < 3; j++)
			sum = sum + x[i][j];

	return sum;
}
