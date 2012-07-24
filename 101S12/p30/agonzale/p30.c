/*************************************/
/* Programmers: Alexander Gonzalez   */
/*                                   */
/* Assignment: Call by reference     */
/*                                   */
/* Completion time: 10 min           */
/*************************************/

#include <stdio.h>

void swap (int *a, int *b);

int main ( int argc, char* argv []) {

    int x,y;
    
    printf(" Enter two numbers for x & y:\n");
    scanf("%d%d", &x,&y);

    swap( &x, &y);

    return 0;
}

void swap( int*a, int *b) {

    int temp;
    
    temp = *a;
    *a = *b;
    *b = temp;

    printf("x = %d\n", *a);
    printf("y = %d\n", *b);


    return;
}
