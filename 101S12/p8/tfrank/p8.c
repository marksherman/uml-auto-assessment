/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 8:One Horrizontal Line of ***       */
/*                                            */
/*Approximate completion time: 30 minutes     */
/**********************************************/

#include <stdio.h>
int main ()

{
	int i, x;
	FILE * fin;
	fin = fopen("testdata8","r");
	fscanf(fin,"%d",&x);
	for(i=0; i<x; i++)
		printf("*",i);
	putchar('\n');
	fclose(fin);
	return 0;
}

