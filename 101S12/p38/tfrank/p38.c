/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 38:Recursive Digit Sum              */
/*                                            */
/*Approximate completion time: 30 minutes     */
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
	if (x < 10)
		return x;
	else 
		return x % 10 + digitsum(x / 10);
}
