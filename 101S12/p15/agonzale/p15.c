/**************************************/
/* Programmer: Alexander Gonzalez     */
/*                                    */
/* Assignment: Solid Box Of Asterisks */
/*                                    */
/* Completion Time: 1 hour            */
/**************************************/

#include <stdio.h>

int main (int argc, char* argv[]) {

    int L, H, x, i;

    printf("Please Enter Two Positive Numbers A Length And Width:\n");
    scanf ("%d%d", &L, &H);

    for ( x = 0; x < L; x++) {
	
	printf("\n");

	for( i = 0; i < H; i++)
	
	    printf("*");
    }

    printf("\n");

    return 0;

}
