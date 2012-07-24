/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 27:Reverse                          */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>

int main (int argc, char*argv[])
{
	int x, z;
	x = z = 0;
	int y[10];
	printf("Please enter 10 values\n");
	for(x=0; x<10; x++){
		scanf("%d",&y[x]);
	}
	for(z=9; z>=0; z--)
		printf("%d ", y[z]);
	putchar('\n');
	return 0;
}

