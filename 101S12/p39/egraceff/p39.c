/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Recursive Persistence                                            */
/*                                                                           */
/* Approximate completion time: 2 hours                                      */
/*****************************************************************************/

#include <stdio.h>
int product(int value);
int persistence(int number);
void input_integer(); 
int main( int argc, char *argv[])
{
  printf("Please enter the integer of which you wish to know the persistence: \n");
  /* prompts the user for an integer */
  input_integer();
  /* calls the input_integer function */
  return 0;
} 
void input_integer( ){
  int number;
  if (scanf("%d", &number) == EOF){
    return ;
    /* if the value scanned into the variable, number, is EOF, return to main */
  }
  else{
    printf("%d\nPlease enter another integer of which you wish to know the persistence: \n", persistence(number));
    input_integer();
    /* otherwise, if an integer is input, the function will be called again, returning the persistence and prompting the user for another value */
  }
}
int persistence(int number){
  if ((number/10) <= 0){
    return 0; 
    /* returns 0 if the value in variable, number, has only one digit */
  }
  else{
    return ( 1 + persistence(product(number))) ;
    /* otherwise, the persistence function calls the product function, adding one to the persistence each time, until the base case is reached */
  }
}
int product(int value){
  if ((value/10) <=0){
    return value;
    /* returns the value of the variable, value, if it is a single digit */
  }
  else{
    return((value%10) * product(value/10));
    /* otherwise, the product function is called, multiplying the last digit of value each time and dividing by ten, until the base case is reached */
  }
}
