/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 28:Digit Sum                        */
/*                                            */
/*Approximate completion time: 40 minutes     */
/**********************************************/

#include <stdio.h>

int digitsum( int a);

int main (int argc, char*argv[])
{
	int x, y;
	x = y = 0;
	FILE *fin;
	fin = fopen (argv[1],"r");
	if (fin == NULL){
		printf("Not a valid file\n");
	}
	else{
		while(fscanf( fin, "%d", &x) != EOF){	
			y = digitsum(x);
			
			printf("For %d, the digit sum is %d\n", x, y);
		}
	fclose(fin);
	}
	return 0;
}

int digitsum( int x)
{

	int z, a;
	a = z = 0;
	while(x != 0){
		z = (x % 10);
		x = (x / 10);
		a = a + z;
	}
	return a;
}
