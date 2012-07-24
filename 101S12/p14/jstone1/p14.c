

/**********************************************/
/*                                            */
/* Programmer: Josh Stone                     */
/*                                            */
/* Program: P14- Sine Function                */
/*                                            */
/* Approx. Completion Time: 15 Mins.          */
/*                                            */
/**********************************************/


#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main (int argc, char* argv []){

    char* val;

    double val2;

    double val3;

    val = argv[1];

    val2 = atof(val);

    val3 = sin(val2);

    printf("%lf\n",val3);

    return 0;

}
