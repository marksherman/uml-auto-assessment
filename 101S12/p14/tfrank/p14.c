/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 14:Sine Function                    */
/*                                            */
/*Approximate completion time: 30 minutes     */
/**********************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
int main (int argc, char*argv[])

{
	int x;
	x= atof(argv[1]);
	printf("The sin is %f\n",sin(x));
	return 0;
}

