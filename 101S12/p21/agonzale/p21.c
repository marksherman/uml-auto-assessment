/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: scanf returns what?  */
/*                                  */
/* Completion time: 30 min          */
/************************************/

#include <stdio.h>

int main (int argc, char* argv []) {
    
    FILE *testdata21;
    int numbers [20];
    int x = 0;
    int y;

    testdata21 = fopen ("testdata21", "r");

    while ( fscanf( testdata21, "%d", &numbers[x]) !=EOF) {
	x++;
	
    }
    printf("the numbers are:\n");

    for (y = 0 ; y < x ; y++) {
	printf("%d\n", numbers[y]);
    }

    fclose(testdata21);

    return 0;
}
