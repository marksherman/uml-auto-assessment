/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 4: The fscanf Function              */
/*                                            */
/*Approximate completion time: 30 minutes     */
/**********************************************/

#include <stdio.h>
int main ()

{
	int x;
	FILE * fin;
	fin = fopen("testdata4","r");
	fscanf(fin,"%d",&x);
	printf("%d\n",x);
	fclose(fin);
	return 0;
}

