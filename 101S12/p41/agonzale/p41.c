/**********************************************************/
/* Programmer: Alexander Gonzalez                         */
/*                                                        */
/* Assignement: Malloc up space for a 1 dimensional array */
/*                                                        */
/* Completion: 30 minutes                                 */
/**********************************************************/

#include <stdio.h>
#include <stdlib.h>

int main (int argc, char* argv[]) {

    int *var_ptr;
    int array[4];
    int n;

    printf("Enter 4 integers:\n");

    var_ptr = (int*)malloc(n * sizeof(int));

    *var_ptr = 0;
    n = 0;
    while( n < 4) {
	scanf("%d", &array[n]);
	*var_ptr = *var_ptr + array[n];
	n++;
    }

    printf("The Sum Is: %d\n", *var_ptr);
    free(var_ptr);

    return 0;
}
