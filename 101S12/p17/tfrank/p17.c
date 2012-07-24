/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 17:Area of a Rectangle              */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>
int main (int argc, char*argv[])

{
	float l, h, area;
	printf("Enter and Lenth and Height\n");
	scanf("%f %f", &l, &h);
	area = l * h;
	printf("The area of the rectangle is %f\n",area);
	return 0;
}

