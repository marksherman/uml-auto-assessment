/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 15:Solid Box of Asterisks           */
/*                                            */
/*Approximate completion time: 30 minutes     */
/**********************************************/

#include <stdio.h>
int main (int argc, char*argv[])

{
	int l, h, i, j;
	printf("Enter a Length and Height\n");
	scanf("%d%d",&l,&h);
	for(i=0;i<h;i++){
		for(j=1;j<l;j++){
			printf("*");
		}
		printf("*\n");
	}
	return 0;
}

