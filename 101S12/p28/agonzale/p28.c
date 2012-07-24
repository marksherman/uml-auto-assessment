/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Digit Sum            */
/*                                  */
/* Completion time: 30 min          */
/************************************/

#include <stdio.h>
#include <stdlib.h>
int main (int argc, char* argv[]) {

    int num;
    int sum = 0;
    int a;

    FILE *testdata28;
    testdata28 = fopen(argv[1], "r");

    while ( fscanf(testdata28, "%d", &num) != EOF){
	a++;
    }

    while ( argv[1] != 0 ) {
	a = num % 10;
	num = num / 10;
	sum = sum + a;
    }
    fclose(testdata28);
    
    return sum;

}
