#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {

    int *var_ptr;
    int array[20][20];
    int r, c, row, col;
    int sum, total;
	      
    printf("Enter any row and colums of numbers\n");
    for(r = 0;r < row; r++) {
	for(c = 0; c < col; c++) {
	    scanf("%d",&array[r][c]);
	}
    }

    printf("Enter which row you would like to sum:\n");
    for (r = 0; r < 20; ++r) {
	sum = 0;
	for (c = 0; c < 20; ++c)
	    scanf("%d",&array[r]);
	    sum = sum + array[r][c];
	printf("row sum [%d][] = %d\n", r, sum);
    }
    printf("Enter which column you would like to sum:\n");
    for (c = 0; c < 20; ++c) {
	sum = 0;
	for (r = 0; r < 20; ++r)
	    scanf("%d",&array[c]);
	    sum = sum + array[r][c];
	printf("column sum [][%d] = %d\n", c, sum);
    }

    var_ptr = (int*)malloc(r * c * sizeof(int));
    *var_ptr = 0;
    total=0;
    while (total < 20) {
	scanf("%d", &array[total]);
	*var_ptr = *var_ptr + array[total][sum];
	total++;
    }
    
    printf("The total of all the integers is: %d\n", *var_ptr);
    free(var_ptr);

    return 0;
}
