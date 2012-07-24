/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 041: Malloc up Space for a 1-Dimensional Array...*/
/*                                                          */
/* Approximate completion time: 015 minutes                 */
/************************************************************/


#include <stdlib.h>
#include <stdio.h>


int main(int argc, char* argv[])
{
    int sum = 0, array_length, i;
    int* array_ptr;

    printf("Please enter array length:\n");
    scanf("%d", &array_length);
    
    array_ptr = (int*)malloc(array_length * sizeof(int));

    printf("Please input %d numbers:\n", array_length);
    
    for(i = 0; i < array_length; i++)
	scanf("%d", &array_ptr[i]);
    
    for(i = 0; i < array_length; i++)
	sum += array_ptr[i];

    printf("The sum of the array is: %d\n", sum);

    free(array_ptr);

    return 0;
}
