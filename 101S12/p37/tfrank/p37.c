/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 37:Digit Sum redux                  */
/*                                            */
/*Approximate completion time: 10 minutes     */
/**********************************************/

#include <stdio.h>

int digitsum( int a);

int main (int argc, char*argv[])
{
	int x, y;
	x = y = 0;
	FILE *fin;
	fin = fopen (argv[1],"r");
		while(fscanf( fin, "%d", &x) != EOF){	
			y = digitsum(x);
			
			printf("For %d, the digit sum is %d\n", x, y);
		}
	fclose(fin);

	return 0;
}

int digitsum( int x)
{

	int end_num, sum;
	sum = end_num = 0;
	while(x != 0){
		end_num = (x % 10);
		x = (x / 10);
		sum = sum + end_num;
	}
	return sum;
}
