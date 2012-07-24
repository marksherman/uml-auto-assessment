/*************************************/
/* Programmer: Alexander Gonzalez    */
/*                                   */
/* Assignment: Reverse               */
/*                                   */
/* Completion time: 30 min           */
/*************************************/

#include <stdio.h>

int main (int argc, char* argv[]) {

    int numbers [10];
    int x=0;

    for (x = 0; x < 10; x++){
	printf("enter 10 numbers:\n");
	scanf("%d",&numbers[x]);
    }
    
    printf("the numbers in reverse order are:\n");
    
    for (x=10-1; x>=0; x--){
	printf("%d\n", numbers[x]);
    }
    return 0;
}
