/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Sine Function        */
/*                                  */
/* Completion Time: 45 min          */
/************************************/

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int main (int argc, char* argv[]) {

    float x, y;

    x = atof(argv[1]);
    y = sin(x);

    printf("Sine Of Number: %f\n", y);

    return 0;

}
