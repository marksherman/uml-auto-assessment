/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Recursive Digit Sum  */
/*                                  */
/* Completion Time: 30 min          */
/************************************/

#include <stdio.h>
#include <stdlib.h>

int digitsum(int num);

int num;

int main (int argc, char* argv[]) {

    FILE *testdata38;
    testdata38 = fopen(argv[1], "r");

    while ( fscanf( testdata38, "%d", &num) != EOF){
        printf("The sum is %d\n", digitsum(num));
    }
    fclose (testdata38);

}

int digitsum( int num ) {

    if ( num == 0 )
	return 0;
    else
	return (num%10 + digitsum(num/10));
}
