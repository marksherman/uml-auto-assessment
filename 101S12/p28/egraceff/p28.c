/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Digit Sum                                                        */
/*                                                                           */
/* Approximate completion time: 45 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int digitsum(int number);
int main( int argc, char *argv[] )
{
  int number;
  int sum;
  FILE *fin;
  fin = fopen(argv[1], "r");
  /* opens the file */
  fscanf(fin, "%d", &number);
  /* reads the integer from the file and stores it into the variable, number */
  sum = digitsum(number);
  /* calls the function digitsum*/
  printf("The sum of the digits that make up the integer %d is %d.\n", number, sum);
  /* prints the final results */
  fclose(fin);
  return 0 ;
}
int digitsum(int number){
  int x;
  int sum=0;
  while(number!=0){
  /* runs the loop until the integer is less zero. */
    x = number%10;
    /* let x be the remainder when dividing number by ten */
    number = number/10;
    /* divide number by ten in order to account for which digits have been summed.*/
    sum = sum + x;
    /* adds the remainders after dividing by ten */
  }
  return sum;
  /* returns the sum of the digits to main */
}

