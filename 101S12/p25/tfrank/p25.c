/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 25:Unfilled Box                     */
/*                                            */
/*Approximate completion time: 30 minutes     */
/**********************************************/

#include <stdio.h>

int main (int argc, char*argv[])
{
	int l, h, i, j;
	char g;
	printf("Enter a Length and Height\n");
	scanf("%d%d",&l,&h);
	for(i=0; i<h; i++){
		if (i == 0)
			g ='*';				
		else if (i == (h-1))
			g = '*';
		else 
			g = ' ';
		for(j=0; j<l; j++){
			if (j == 0)
				printf("*");
			else if( j == (l-1) )
				printf("*");
			else
				printf("%c",g);
		}
		putchar('\n');       
	}
	return 0;
}

