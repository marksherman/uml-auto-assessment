/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 036: Persistence of a Number                     */
/*                                                          */
/* Approximate completion time: 015 minutes                 */
/************************************************************/


#include <stdio.h>


int persist(int num);

int main(int argc, char* argv[])
{
    int inval = 0;

    printf("Enter an integer: ");
    scanf("%d", &inval);

    while(inval != EOF)
    {
   	printf("The persistence of the number is %d.\n", persist(inval));
	printf("Enter an integer: ");
        scanf("%d", &inval);
    }
    
    return 0;
}


int persist(int num)
{
    int num_persist = 0, product;
    
    while((num / 10) > 0)
    {
	product = 1;
	while(num > 0)
	{
	    product *= (num % 10);
	    num /= 10;
	}
	num = product;
	num_persist++;
    }
    return num_persist;
}
