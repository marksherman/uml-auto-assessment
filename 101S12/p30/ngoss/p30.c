/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 030: Simulating Call By Reference                */
/*                                                          */
/* Approximate completion time: 5 minutes                   */
/************************************************************/


#include <stdio.h>

void swap(int* a, int* b);

int main(int argc, char* argv[])
{
    int x, y;
    
    printf("Input two integer values: ");
    scanf("%d %d", &x, &y);
    
    swap(&x, &y);

    printf("%d %d\n", x, y);

    return 0;
}


void swap(int* a, int* b)
{
    int temp;

    temp = *a;
    *a = *b;
    *b = temp;

    return;
}
