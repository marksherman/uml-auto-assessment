/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Persistence of a Number                                          */
/*                                                                           */
/* Approximate completion time: 3 hours                                      */
/*****************************************************************************/

#include <stdio.h>
int product(int value);
int persistence(int number);
int main( int argc, char *argv[])
{
  int number;
  int x;
  printf("Please enter the integer of which you wish to know the persistence: \n");
  /* prompts the user for an integer */
  scanf("%d", &number);
  /* scans said integer into the variable number */
  while(getchar() != EOF){
  /* runs the loop until EOF is reached */
    x = persistence(number);
    /* calls the persistence function to calculate the persistence of the number */
    printf("%d\n", x);
    /* prints the persistence of the number */
    scanf("%d", &number);
    /* scans in the next integer which the user inputs in order to repeat the process */
  }
  return 0 ;
}
int persistence(int number){
  int i;
  for(i=0; (number/10) > 0  ; i++){
  /* runs the loop until number is a single digit */
      number = product(number);
      /* calls the product function to calculate the product of the current number */
  }   
  return i;
  /* return the number of times the product is calculated, in other words the persistence, to main */
}
int product(int value){
  int y, product = 1;
  while(value!=0){
  /* runs the loop while the integer is greater than zero */
    y = value%10;
    /* let y be the remaining digit when dividing the integer by 10 */
    value = value/10;
    /* divides the integer by ten in order to account for which digits have been multiplied */
    product = product *y;
    /* multiplies the peeled off integer by the current product until the digits have all been multiplied */
  }
  return product;
  /* returns the product to the persistence function */
}

