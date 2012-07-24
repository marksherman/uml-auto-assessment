/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Area Of Rectangle    */
/*                                  */
/* Completion time: 45 min          */
/************************************/

#include <stdio.h>

int main ( int argc, char* argv[]) {

    float l, w, area;

    printf("Please Enter The Desired Length and Width Of The Rectangle:\n");
    scanf("%f%f", &l,&w);

    area = l * w;
    printf("The Area Of The Rectangle Is: %f\n",area);

    return 0;

}
