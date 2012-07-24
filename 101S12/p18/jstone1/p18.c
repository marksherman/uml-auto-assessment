

/***********************************************/
/*                                             */
/* Programmer: Josh Stone                      */
/*                                             */
/* Program: P18 - Area of a Circle             */
/*                                             */
/* Approx. Completion Time: 30  mins.          */
/*                                             */
/***********************************************/


#include <stdio.h>
#include <math.h>


int main (int argc, char* argv []){

    float r;

    float area;

    printf("Please input the radius of the circle:");

    scanf("%f",&r);

    area = ( (r * r) * M_PI);

    printf("The area of a circle with a radius of %f is %f.\n",r,area);

    return 0;

}

    
