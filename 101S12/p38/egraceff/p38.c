/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Recursive Digit Sum                                              */
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
  if ((number/10) <=0){
    return number;
    /* returns the value of the number if it is a single digit */
  }
  else{
    return((number%10) + digitsum(number/10));
    /* otherwise, the digitsum function is called, adding the last digit of number each time and dividing number by ten, until the base case is reached */
  }
}
