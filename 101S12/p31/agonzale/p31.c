/***********************************************/
/* Programmer: Alexander Gonzalez              */
/*                                             */
/* Assignment: inner products of two vectors   */
/*                                             */
/* Completion time: 30 min                     */
/***********************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char* argv[]){
    int sum = 0;
    int size = 8;
    float u[8], v[8];

    printf("enter first vector:\n");
    scanf("%d", &u[size]);
    
    printf("enter second vector:\n");
    scanf("%d", &v[size]);

    printf("the sum of the vectors are: %d\n", sum);

    return 0;
}

float inner( float u[] , float v[], int size ){
    
    int sum = 0;
    int i;

    for (i = 0;i<size; i++)
 
	sum += u[i]*v[i];

    return sum;
}
