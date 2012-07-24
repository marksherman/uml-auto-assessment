/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Digit Sum (re-done)  */
/*                                  */
/* Completion time: 30 min          */
/************************************/

#include <stdio.h>
#include <stdlib.h>

int digitsum(int num);
int num;
int a;
int sum = 0;

int main (int argc, char* argv[]) {

    FILE *testdata28;
    testdata28 = fopen(argv[1], "r");

    while ( fscanf( testdata28, "%d", &num) != EOF){
        printf("The sum is %d\n", digitsum(num));
    }
    fclose (testdata28);

    return 0;
}

int digitsum( int num ) {

    while ( num > 0 ) {
        a = num % 10;
        num = num / 10;
        sum = sum + a;
    }

    return sum;

}
