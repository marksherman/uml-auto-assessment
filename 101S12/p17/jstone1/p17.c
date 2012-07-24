


/************************************************/
/*                                              */
/* Programmer: Josh Stone                       */
/*                                              */
/* Program: P17 - Area of a Rectangle           */
/*                                              */
/* Approx. Completion Time: 10 Mins.            */
/*                                              */
/************************************************/



#include <stdio.h>

int main (int argc, char* argv []){
 
    float length;
  
    float height; 

    float area;

    printf("Please enter the length of the rectangle in feet:");

    scanf("%f",&length);

    printf("Please enter the height of the rectangle in feet:");

    scanf("%f",&height);

    area = (length * height);

    printf("The area of this rectangle is %f square feet.\n",area);

    return 0;

}
