/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 20:Reverse the comman line          */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>
int main (int argc, char*argv[])

{
	int i;
	for(i= (argc-1); i>=0; i--){
		printf(argv[i]);
		putchar('\n');
	}
	return 0;
}

