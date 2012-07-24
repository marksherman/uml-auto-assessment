/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Area Of Circle       */
/*                                  */
/* Completion Time: 45 minutes      */
/************************************/

#include <stdio.h>
#include <math.h>

int main (int argc, char* argv[]) {

    float radius, area;

    printf("Enter The Radius Of The Circle:\n");
    scanf("%f", &radius);

    area = M_PI * radius * radius;
    
    printf("The Area Of The Circle Is: %f\n",area);

    return 0;
}
