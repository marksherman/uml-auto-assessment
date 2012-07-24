/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 26:One Dimensional Array            */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>

int main (int argc, char*argv[])
{
	int z, i;
	z = i = 0;
	int y[15];
	FILE *fin;
	fin = fopen("testdata26","r");
	while(fscanf ( fin, "%d", &y[i]) != EOF){
		i++;
	}
	for( z = 14; z>=0; z--){
		
		printf("%d ", y[z]);
	}
	putchar('\n');
	fclose(fin);
	return 0;
}

