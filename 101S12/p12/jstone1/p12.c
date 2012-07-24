

/********************************************/
/*                                          */
/* Programmer: Josh Stone                   */
/*                                          */
/* Program: P12 - SQRT Function             */
/*                                          */
/* Approx. Completion Time: 15 mins         */
/*                                          */
/********************************************/


#include <stdio.h>
#include <math.h>

int main (int argc, char* argv []){

float value;

double root;

printf("Please enter a number:");

scanf("%f",&value); 

root = sqrt(value);

printf("The square root of the number is: %lf \n ", root) ;

     return 0;

}

