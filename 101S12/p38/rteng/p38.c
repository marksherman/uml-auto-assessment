/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p38: Recursive Digit Sum                      */
/*                                                       */
/* Approximate completion time: 18 minutes               */
/*********************************************************/
#include <stdio.h>
int digitsumfunction (int input);
int main(int argc, char* argv[])
{
  int input, digitsum;
  FILE *fin;
  fin = fopen (argv[1], "r");
  /*Allows opening of file with argv[1] from command line*/
  while((fscanf (fin, "%d", &input)) != EOF ){
    digitsum = digitsumfunction (input);
    /*Directs the input to separate function.*/
    printf ("The sum of the individual digits in the file of one of the numbers is %d.\n", digitsum);
  }
  /*The printf in the "while" loop allows the sum of EVERY number in the file to be printed out and not just the last one.*/
  fclose (fin);
  return 0;
}
int digitsumfunction (int input)
{
  if (input == 0)
    return 0;
  /*Provides case if input is 0 so that the sum of digits will simply be the inputted number.*/
  return (input % 10 + digitsumfunction (input / 10));
  /*If the input is not 0, it will call the function again after finding the next digit to compute the sum of all the digits in that one number.*/
}

