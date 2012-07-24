/**********************************************/
/* Programmer: Alexander Gonzalez             */
/*                                            */
/* Assignment: Positive, Negative, Zero       */
/*                                            */
/* Completion Time: 30 min                    */
/**********************************************/

#include <stdio.h>

int main() {

    int x;
    printf("Enter Any Number:\n");
    scanf("%d", &x);

    if(x==0) {
	printf("The Number Is Zero\n");
    }
    else {
	if(x<0) {
	    printf("The Number Is Negative\n");
	}
	if(x>0){
	    printf("The Number Is Positive\n");
	}
    }
    return 0;
}
