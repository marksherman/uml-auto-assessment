/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Bigger Than 100      */
/*                                  */
/* Completion Time: 30 min          */
/************************************/

#include <stdio.h>

int main(){

    int number;

    printf("Please Enter Any Number: \n");

    scanf("%d", &number);

    if (number>100)
	
	printf("The Number IS Bigger Than 100.\n");
    else
	printf("The Number Is NOT Bigger Than 100.\n");

    return 0;

}
