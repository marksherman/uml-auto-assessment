/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Digit Sum(again)                                                 */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
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
  while (fscanf(fin, "%d", &number) !=EOF){
  /* reads an integer from the file and stores it into the variable, number, until there are no more numbers in the file to read in */
    sum = digitsum(number);
    /* calls the function digitsum */
    printf("%d ", sum);
    /* prints the final results */
  }
  printf("\n");
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

