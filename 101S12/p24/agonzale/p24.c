/*************************************/
/* Programmer: Alexander Gonzalez    */
/*                                   */
/* Assignment: Find The Average      */
/*                                   */
/* Completion Time: 30 min           */
/*************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main (int argc, char* argv[]) {

    FILE *testdata24;
    int x, y, z;
    float average = 0;

    testdata24 = fopen ("testdata24", "r");

    while ( y = 0 !=EOF){
	z = fscanf (testdata24, "%d", &x);
	if (z == EOF){
	    break;
	}
	average = average + x;
    }
    average = average / 4;
    printf("The Average Is: %f\n", average);
    fclose (testdata24);

    return 0;
}
