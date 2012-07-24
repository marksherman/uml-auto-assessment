/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 21:scanf returns what?              */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>

int main ()
{
	int x;
	x = 0;
	FILE * fin;
	fin = fopen("testdata21","r");
	while( fscanf( fin, "%d", &x) != EOF){
		
	       	printf("%d ",x);
		
		}
	fclose (fin);
	putchar('\n');
	return 0;
}

