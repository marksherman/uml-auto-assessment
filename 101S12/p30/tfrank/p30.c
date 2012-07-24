/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 30:Simulating Call by Reference     */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>

void swap( int *a, int *b);

int main (int argc, char*argv[])
{
	int x, y;
	x = y = 0;

	printf("Enter two values x and y\n");
	scanf("%d%d",&x,&y);

	swap ( &x, &y);

	printf("x became %d and y became %d\n", x, y);
	return 0;

}

void swap( int *a, int *b) 
{
	int c;

	c = *a;
	*a = *b;
	*b = c;

	return;
}
