/**********************************/
/* Programmer: Alexander Gonzalez */
/*                                */
/* Assignment: Equal to Zero?     */
/*                                */
/* Completion Time: 20 min        */
/**********************************/

#include <stdio.h>

int main() {

    int x;
    printf("Please enter a number to test\n");
    scanf("%d", &x);

    if(x==0){
	printf("The Number IS equal to Zero\n");
    }
    else{
	printf("This Number is NOT equal to Zero\n");
    }
    return 0;
}
