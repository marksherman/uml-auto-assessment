/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 043: Square Deal                                 */
/*                                                          */
/* Approximate completion time: 2 hours 30 minutes          */
/************************************************************/


#include <stdlib.h>
#include <stdio.h>


int isprime(int n);
int main(int argc, char* argv[])
{
    int i, j, N, middle, initial_value, x, y, bound = 1;
    int* array_ptr;

    printf("Input the array size now: ");
    scanf("%d", &N);

    printf("Input the initial value: ");
    scanf("%d", &initial_value);

    array_ptr = (int*)malloc(N * N * sizeof(int));

    x = (N - (N/2) - 1);
    y = x;
    middle = x;

    do{
	/* The following checks if the value is prime */
	if(isprime(initial_value) == 0)
	    array_ptr[x*N + y] = -1;
	else
	    array_ptr[x*N + y] = initial_value;
	
	/* End of prime check */

	initial_value++;

	/* The following controls the movement around the array */
	if(x == (N - 1) && y == (N - 1))
	    x++;
	else if(x == middle && y == middle)
	    x++;
	else if(x > middle && y > middle)
	{
	    if(x == (middle + bound))
		y--;
	    else
		x++;
	}
        else if(x > middle && y < middle)
	{
	    if(y == (middle - bound))
		x--;
	    else
		y--;
	}
	else if(x < middle && y < middle)
	{
	    if(x == (middle - bound))
		y++;
	    else
		x--;
	}
	else if(x < middle && y > middle)
	{
	    if( (x + y) > (N - 1) )
		x++;
	    else if(y == (N - 1))
		x++;
	    else if(y == (middle + bound) )
	    {
		x++;
		bound++;
	    }
	    else
		y++;
	}
	else if(x == middle)
	{
	    if(y > middle)
		x++;
	    else
		x--;
	}
	else if(y == middle)
	{
	    if(x > middle)
		y--;
	    else
		y++;
	}
	
	/* End of movement section */
    }while( x != N && y != N );/* End of while() */

    /*The following section prints out the array */
    
    for(i = 0; i < N; i++)
    {
	for(j = 0; j < N; j++)
	{
	     if(array_ptr[j*N + i] == -1)
		 printf("     ***");
	     else
		 printf("%8d", array_ptr[j*N + i]);
	     
	}
	printf("\n");
    }
    /*End of print section */

    free(array_ptr);
    
    return 0;
}

int isprime(int x)
{
    int i, prime = 1;
    if(x == 2 || x == 3 || x == 5 || x == 7)
	prime = 1;
    else if(x == 1)
	prime = 0;
    else
	for(i = 9; i >= 2; i--)
	    if((x % i) == 0)
		prime = 0;

    return prime;
}
