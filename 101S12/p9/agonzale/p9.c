/**************************************/
/* Programmer: Alexander Gonzalez     */
/*                                    */
/* Assingment: Using a FOR loop       */
/*                                    */
/* Completion time: 30 min            */
/**************************************/

#include <stdio.h>

int main () {

    int x, y, z, a, b;
    
    FILE *testdata9;

    testdata9 = fopen("testdata9", "r");

    for( x = 0; x < 1; x++) {

	fscanf(testdata9, "%d%d%d%d%d", &x, &y, &z, &a, &b);

	printf("%d\n%d\n%d\n%d\n%d\n", x, y, z, a, b);
    }

    return 0;
}
