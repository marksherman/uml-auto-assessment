/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 18:Area of a Circle                 */
/*                                            */
/*Approximate completion time: 20 minutes     */
/**********************************************/

#include <stdio.h>
#include <math.h>
int main (int argc, char*argv[])

{
	float r, area;
	printf("Please enter a radius\n");
	scanf("%f", &r);
	area = r * r * M_PI;
	printf("The area of the circle is %f\n",area);
	return 0;
}

