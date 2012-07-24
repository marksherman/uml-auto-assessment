/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 9:Using a for Loop                  */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>
int main ()

{
	int i, x;
	FILE * fin;
	fin = fopen("testdata9","r");
	for(i=0; i<5 ;i++){
		fscanf(fin,"%d",&x);
	       	printf("%d ",x);
	}
	fclose(fin);
	putchar('\n');
	return 0;
}

