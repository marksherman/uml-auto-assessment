/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 33:Recusive Factorial               */
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

	x = fact ( x );
	printf("The factorialized version of your number is %d\n", x);

	return 0;
}

int fact( int n){
	
	if ( n == 1)
		return 1;
	else
		return (n * fact(n - 1));
}
