/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 032: Non-recursive Factorial-Use a function!     */
/*                                                          */
/* Approximate completion time: 010 minutes                 */
/************************************************************/


#include <stdio.h>

int factorial(int val);

int main(int argc, char* argv[])
{
    int val;

    printf("Enter an integer: ");
    scanf("%d", &val);

    printf("The factorial of %d is %d\n", val, factorial(val));
    
    return 0;
}


int factorial(int val)
{
    int fact = 1, i;

    for(i = val; i > 1; i--)
    {
	fact *= i;
    }

    return fact;
}
