/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p36: Persistence of a Number                  */
/*                                                       */
/* Approximate completion time: 32 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int argc, char* argv[])
{
  int i = 0, digit = 0, product = 0;
  while (digit != EOF){
    if(digit == EOF)
      break;
    /*Provides exit case.*/
    i = 0;
    printf("Enter a digit to find the persistence of: ");
    /*Ask for user input.*/
    scanf("%d", &digit);
    /*Reads in digit from user input.*/
    while (digit != 0){
      product *= digit % 10;
      digit /= 10;
      /*Performs multiplication of digits til one digit left.*/
      i++;
      /*Counts number of times multiplication is performed*/
    }
    printf("The persistence of the number is %d.\n", i);
    /*Prints out the persistence of the number and begins loop again*/
  }
  return 0;
}
