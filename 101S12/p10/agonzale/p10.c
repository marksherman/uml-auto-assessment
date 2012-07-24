/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Sum of twenty        */
/*                                  */
/* Completion Time: 30 min          */
/************************************/

#include <stdio.h> 

int main () {

    int sum, a, b ;

    FILE *testdata10;

    testdata10 = fopen("testdata10", "r");

    for (sum=0; sum<1 ; sum++) {

	fscanf( testdata10, "%d", &a);

	sum =  a + b ;
	
        printf("\nSum Of Numbers From File = %d\n\n",sum);
    }
    return 0;
}
