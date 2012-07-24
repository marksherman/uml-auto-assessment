/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 39:Recursive Persistence            */
/*                                            */
/*Approximate completion time: 40 minutes     */
/**********************************************/

#include <stdio.h>

int persistence( int a, int *c);

int multiplied( int b);

int scan();

int main (int argc, char*argv[]){
	scan();
	return 0;
}

int scan(){
	int x, y;
	x = y = 0;
	int count = 0; 
	printf("Enter a number\n");	

	if( scanf("%d", &x) != EOF){
	
		y = persistence(x, &count);
			
		printf("For %d, the persistence is %d\n", x, y);

		count = 0;
		scan();
	}
 
	return 0;
}

int persistence( int x, int *count)
{
	if(x > 9 ){
		x = multiplied( x );
		
		(*count) ++;
		
		return persistence ( x, count );
	}
	else 
		return *count; 
		
}

int multiplied( int x)
{
	
	if (x > 9){
		return x % 10 * multiplied( x / 10);
	}
	else
		return x;
}
