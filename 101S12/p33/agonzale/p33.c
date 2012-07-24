/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Recursive Factorial  */
/*                                  */
/* Completion time: 15 min          */
/************************************/

#include <stdio.h>
#include <stdlib.h>

int main ( int argc, char* argv[]) {

    int x;

    printf("enter an integer:\n");
    scanf("%d", &x);

    printf("The factorial of %d is = %d\n", x, fact(x));
}

int fact (int x) {

    if (x == 1)
	return 1;
    else
	return (x * fact(x-1));
}
