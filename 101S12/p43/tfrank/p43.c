/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 43:Square Deal                      */
/*                                            */
/*Approximate completion time: 60 minutes     */
/**********************************************/

#include <stdio.h>

int isprime(int value);
int main (int argc, char*argv[])
{
	int row, col, i, n, value, move;
	int array[15][15];
	move = 1;

	printf("Enter the size of the box created (odd number 3 - 15)\n");
	scanf("%d", &n);
	printf("Enter the initial value\n");
	scanf("%d", &value);

	putchar('\n');

	row = n / 2;
	col = n / 2;
	while( move < n){
		for(i = 0; i < move; i++, col++, value++)
			array[row][col] = value;
		for(i = 0; i < move; i++, row--, value++)
			array[row][col] = value;
		move++;
		for(i = 0; i < move; i++, col--, value++)
			array[row][col] = value;
		for(i = 0; i < move; i++, row++, value++){
			array[row][col] = value;
		}
		move++;
	}	    
	if(move == n){
		for(i = 0; i < n; i++, col++, value++)
			array[n-1][col] = value;     
	}	
	for(row = 0; row < n; row++){
		for(col = 0; col < n; col++){
			if( isprime(array[row][col]) == 1)
				printf("%3d ", array[row][col]);
			else
				printf("*** ");
		} 
		putchar('\n');
	}


	putchar('\n');

	return 0;
}

int isprime(int value) {
	int c;
	if(value == 1)
		return 0;
	else
		for(c = 2; c < value; c++){
			if(value % c == 0)
				return 0;
		}

	return 1;
}

