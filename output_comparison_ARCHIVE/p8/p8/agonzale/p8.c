/*************************************************/
/* Programmer: Alexander Gonzalez                */
/*                                               */
/* Assignment: One Horizontal Line of Asterisks  */
/*                                               */
/* Completion time: 40 min                       */
/*************************************************/

#include <stdio.h>

int main() {

    int a, z;
    
    FILE *testdata8;

    testdata8 = fopen("testdata8", "r");

    for (a=0; a < z;  a++){
	
	fscanf (testdata8 , "%d", &z);
    
	printf("*");
    }
    printf("\n");
    
    fclose(testdata8); 

    return 0;
}
