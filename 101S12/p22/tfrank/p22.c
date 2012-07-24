/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 22: Sum of a bunch                  */
/*                                            */
/*Approximate completion time: 10 minutes     */
/**********************************************/

#include <stdio.h>

int main (int argc, char*argv[])
{
	int x, sum;
	sum = 0;
	FILE *fin;

	fin = fopen("testdata22", "r");
	while ( fscanf( fin, "%d", &x) != EOF) {
		sum = sum + x;
	}
	fclose (fin);
	printf("The sum is %d\n", sum);

	return 0;
}

