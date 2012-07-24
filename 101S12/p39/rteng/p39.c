/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p39: Recursive Persistence                    */
/*                                                       */
/* Approximate completion time: 30 minutes               */
/*********************************************************/
#include <stdio.h>
int reducer(int number, int i);
int digitproduct(int number);
void askandprint();
/*Prototypes all functions that will be used*/
int main(int argc, char* argv[])
{
  int number = 0;
  if(number != EOF)
    /*Will call to askandprint function if number is not EOF*/
    askandprint();
  return 0;
}  
void askandprint()
{
  int i = 0, number = 0;
  printf("Enter an integer to find the persistence of it: ");
  /*Ask for user input.*/
  if((scanf("%d", &number) != EOF))
    {
      printf("The persistence of the integer is: %d.\n", reducer(number, i));
      /*Print out the calculated persistence of the integer*/
      askandprint();
    }
}
int reducer(int number, int i)
{
  if(number <= 9){
    return i;
    /*Returns i, once the number of timex the disgit product is calculated is known.*/}
  else{
    /*Continues to compute the digit product if the integer is not one digi only*/
    number = digitproduct(number);
    return reducer(number, i++);
  }
}
int digitproduct(int number)
{
  int product = 0;
  if(number <= 9)
    return number;
  else
    {
      /*Finds the last digit and multiplies it to the others*/
      product *= (number % 10);
      number /= 10;
      return digitproduct(number);
    }
}
