/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 10:Sum of Twenty                    */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>
int main ()

{
	int x, total, i;
	FILE * fin;
	total=0;
	fin = fopen("testdata10","r");
	for(i=0; i<20; i++){
		fscanf(fin, "%d", &x);
		total=x+total;
	}
	printf("The total is %d\n",total);	
	fclose(fin);
	return 0;
}

