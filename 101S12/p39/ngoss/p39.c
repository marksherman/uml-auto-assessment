/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 039: Recursive Persistence                       */
/*                                                          */
/* Approximate completion time: 030 minutes                 */
/************************************************************/


#include <stdio.h>

int recurse_persist(int num, int i);
int recurse_readin(void);
int recurse_digitProduct(int num);

int main(int argc, char* argv[])
{
    recurse_readin();

    return 0;
}


int recurse_readin(void)
{
    int inval;

    printf("Please input a number: ");

    if(scanf("%d", &inval) == EOF)
	return 0;
    else
    {
	printf("The persistence is %d.\n", recurse_persist(inval, 0));
	recurse_readin();
    }

    return 0;
}

int recurse_persist(int num, int i)
{
    if(num > 9)
	return recurse_persist(recurse_digitProduct(num), ++i);
    else
	return i;
}

int recurse_digitProduct(int num)
{
    if(num < 10)
        return num;
    else
        return (num % 10) * recurse_digitProduct(num / 10);
}
