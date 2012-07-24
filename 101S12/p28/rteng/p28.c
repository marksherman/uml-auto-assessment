/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p28: Digit Sum                                */
/*                                                       */
/* Approximate completion time: 22 minutes               */
/*********************************************************/
#include <stdio.h>
int digitsumfunction (int input);
int main(int arc, char* argv[])
{
  int input, digitsum;
  FILE *fin;
  fin = fopen (argv[1], "r");
  /*Allows opening of file with argv[1] from command line*/
  while(fscanf (fin, "%d", &input) != EOF){
    digitsum = digitsumfunction (input);
    /*directs the input to separate function*/
  }
  printf ("The sum of the individual digits in the file is %d.\n", digitsum);
  fclose (fin);
  return 0;
}
int digitsumfunction (int input)
{
  int digitsum;
  while (input != 0){
    digitsum += input % 10;
    input /= 10;
    /*Computes sum of all digits present in the file*/
  }
  return digitsum;
  /*Returns the calculated sum to main function*/
}
