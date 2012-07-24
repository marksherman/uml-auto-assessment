/*************************************/
/* Programmer: Alexander Gonzalez    */
/*                                   */
/* Assignment: One Dimensional array */
/*                                   */
/* Completion time: 45 min           */
/*************************************/

#include <stdio.h>

int main (int argc, char* argv[]) {

    FILE *testdata26;
    int numbers [15];
    int x = 0;
    
    testdata26 = fopen("testdata26", "r");
    while (fscanf(testdata26, "%d", &numbers [x]) !=EOF) {
	    x++;
    }
    printf("the number in reverse are:\n");
    
    for( x=15-1; x>=0; x--){
	printf("%d\n",numbers[x]);
    }
    
    fclose(testdata26);
    
    return 0; 
}
