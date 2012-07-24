/************************************************************/
/* Programmer: Nathan Goss                                  */
/*                                                          */
/* Program 038: Recursive Digit Sum                         */
/*                                                          */
/* Approximate completion time: 030 minutes                 */
/************************************************************/


#include <stdio.h>


int recurse_digitSum(int num);


int main(int argc, char* argv[])
{
    int  val = 0, sum = 0;
    FILE* fin;

    fin = fopen(argv[1],"r");

    while(fscanf(fin, "%d", &val) != EOF)
    {
	sum = recurse_digitSum(val);
	printf("The sum of the digits of the integer %d is %d\n", val, sum);
    }

    fclose(fin);

    return 0;
}


int recurse_digitSum(int num)
{
    if( (num / 10) == 0)
	return num;
    else
	return (num % 10) + recurse_digitSum(num / 10);
}
