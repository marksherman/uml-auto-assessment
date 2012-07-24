/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 36:Persistence of a Number          */
/*                                            */
/*Approximate completion time: 40 minutes     */
/**********************************************/

#include <stdio.h>

int persistence( int a);

int main (int argc, char*argv[])
{
	int x, y;
	x = y = 0;

	printf("Enter a number\n");	

	while( scanf("%d", &x) != EOF){
	
		y = persistence(x);
			
		printf("For %d, the persistence is %d\n", x, y);
	}
	
	return 0;
}

int persistence( int x)
{

	int end_num, a, count;
        count = 0;
	end_num = 1;
	while(x > 9 ){
		a = 1;
		while(x != 0 ){
			end_num = (x % 10);
			x = (x / 10);
			a = a * end_num;
		}
		x = a;
		count ++;
	}
	return count;
}
