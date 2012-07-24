/*************************************/
/* Programmer: Alexander Gonzalez    */
/*                                   */
/* Assignment: Count Characters      */
/*                                   */
/* Completion Time: 1 hour           */
/*************************************/

#include <stdio.h>

int main (int argc, char* argv[]) {

    int x;
    char ch;
    
    x = 0;

    printf("Please Enter Any Series Of Characters:\n");
    
    while ((ch = getchar()) !=EOF) {
	x++;
    }
    printf( "The Text You Entered Contained %i Characters\n",x);

    return 0;
}
