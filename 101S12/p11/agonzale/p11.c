/*************************************/
/* Programmer: Alexander Gonzalez    */
/*                                   */
/* Assignment: The Abs Function      */
/*                                   */
/* Completion Time: 1 hour           */
/*************************************/

#include <stdio.h>
#include <stdlib.h>

int main () {

    int num;

    abs (num);

    printf("Please Enter Either A Zero OR A Positive/Negative Number:\n",num);

    scanf("%d",&num);

    printf("The Absolute Value Of The Number You Entered is: %d\n", abs (num));
    
    return 0;
}
