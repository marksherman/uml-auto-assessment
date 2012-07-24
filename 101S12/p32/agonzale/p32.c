/****************************************/
/* Programmer: Alexander Gonzalez       */
/*                                      */
/* Assignment: Non-Recursive factorial  */
/*                                      */
/* Completion time 30 min               */
/****************************************/

#include <stdio.h>
#include <stdlib.h>

int main ( int argc, char* argv[]) {

    int x,f;

    printf("enter a number:\n");
    scanf("%d", &x);

    f=factorial(x);

    printf("the factorial of the number %d is %d\n", x, f);

}

int factorial ( int num ) {

    int fact=1, i; {
	for(i = 1; i <= num; i++) {
	    fact=(fact*i);
	}
    }
    return (fact);
}
