/*************************************/
/* Programmer: Alexander Gonzalez    */
/*                                   */
/* Assignment: Recursive Persistence */
/*                                   */
/* Completion: 30 min                */
/*************************************/

#include <stdio.h>
#include <stdlib.h>

int main ( int argc, char* argv[]) {

    int x;

    printf("Enter a Positive integer:\n");
    scanf("%d", &x);

    printf("The Persistence of the number you entered is: %d\n", persistence(x) );
    
    return 0;
}
int reduce( int x, int y) {

    if (x <= 9)
	return y;

    else
	return reduce(persistence(x), y++);
}
int persistence( int x ) {

    if ( x > 9)
	return (x%10 * persistence(x/10));
    else
	return 0;
}
