/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 24:Find the average                 */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>

int main (int argc, char*argv[])
{
	int x, i;
	x = i = 0;
	float avg;
	avg = 0;
	FILE *fin;
	fin = fopen ("testdata24", "r");

	while ( fscanf (fin,"%d", &x) != EOF){
		i = x + i;
	}
	fclose( fin );
	printf("The total is %d\n", i);
	avg = (i / 4.0);
	printf("The average is %f\n", avg);

	return 0;
}

