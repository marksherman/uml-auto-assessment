/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 033: Recursive Factorial                         */
/*                                                          */
/* Approximate completion time: 015 minutes                 */
/************************************************************/


#include <stdio.h>

int factorial(int val);

int main(int argc, char* argv[])
{
    int val;

    printf("Input an integer: ");
    scanf("%d", &val);

    printf("The factorial of %d is %d\n", val, factorial(val));

    return 0;
}


int factorial(int val)
{
    if(val == 1)
	return val;
    else
	return val * factorial(val - 1);
}
