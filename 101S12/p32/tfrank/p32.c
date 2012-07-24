/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 32:Non-Recusive Factorial           */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>

int fact ( int n);

int main (int argc, char*argv[])
{
	int x;

	printf("Enter a number to be factorialized\n");
	scanf("%d", &x);

	x = fact( x );

	printf("The factorialized version of your number is %d\n", x);

	return 0;
}

int fact( int n){
	int i, j;
	j = 1;
	for(i = 1; i <= n; i++)
		j = j * i;
	return j;
}
