/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 7:Positive, Negative, or Zero       */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>
int main ()

{
	int i;
	printf("Please enter a number\n");
	scanf("%d",&i);
	if(i>0){
		printf("The number is positive.\n");
	}
	else if(i<0){
		printf("The number is negative.\n");
	}
	else{
		printf("The number is zero.\n");
	}

	return 0;
}

