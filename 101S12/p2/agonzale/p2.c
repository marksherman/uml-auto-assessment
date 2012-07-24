/**************************************/
/* Programmer: Alexander Gonzalez     */
/*                                    */
/* Program 2: The scanf Function      */
/*                                    */
/* Approximate Time: 30 minutes       */
/*                                    */
/**************************************/

#include <stdio.h>

main()
{
    int number;

    printf("Type in a number \n");

    scanf("%d", &number);

    printf("The number you typed was %d\n",number);

    return 0;

}
