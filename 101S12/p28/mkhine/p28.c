/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Digit Sum                        */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include <stdio.h>

int digit_sum (int num){
  static int s = 0;
  s += num;
  return s;
}


int main (int argc, char *argv[])
{
  int sum= 0, num;
  int i = 0;
  FILE *fin;

  fin = fopen (argv[1], "r"); /*opens the file with the name argv[1]  */
  
  
  while (fscanf (fin, "%d", &num) != EOF){
    /* read integers from fin and store in the number */  
    sum = digit_sum(num);
    i++;
  }
  printf ("The sum of digits of number is %d\n", sum);
  


  
fclose (fin);  /*closes the file */

  return 0;
}
