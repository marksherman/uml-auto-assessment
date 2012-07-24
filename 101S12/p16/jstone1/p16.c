
/***********************************************/
/*                                             */
/* Programmer: Josh Stone                      */
/*                                             */
/* Program: P16 - Count Characters             */
/*                                             */
/* Approx. Completion Time: 30 Mins.           */
/***********************************************/


#include <stdio.h>

int main (int argc, char* argv []){

    int count = 0;

    int i;

    printf("Enter Characters:");

    i = 0;

    while(i != EOF){

        i = getchar(); 

        count++;

    }

    count = (count - 1);
 
    printf("\nYou enterted %d characters\n ",count);

    return 0;

}
