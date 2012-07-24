/************************************/
/* Programmer: Alexander Gonzalez   */
/*                                  */
/* Assignment: Unfilled box         */
/*                                  */
/* Completion time: ? Hours         */
/************************************/

#include <stdio.h>

int main ( int argc, char* argv []) {

    int L, H, x, y;

    printf("Enter a length and a width:\n");
    scanf("%d%d", &L,&H);

    for (x=1; x<L; x++){
	printf("*");
    }
    for (y=0; y<H; y++){
	    printf("*\n");
    }
 
    return 0;
}
