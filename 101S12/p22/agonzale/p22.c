/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assingnment: Sum of a Bunch      */
/*                                  */
/* Completion time: 30 Min          */
/************************************/

#include <stdio.h>

int main ( int argc, char* argv []){

    FILE *testdata22;
    int x = 0;
    int sum = 0;

    testdata22 = fopen ("testdata22", "r");

    while ( fscanf (testdata22, "%d," , &x) !=EOF) {

	sum += x;
    }

    printf("Sum of numbers are: %d\n",sum);

    fclose (testdata22);

    return 0;

}
