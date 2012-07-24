/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Digit Sum                        */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include <stdio.h>

int digit_sum (int num){
  int s=0;
  while (num != 0){
    s += num%10;
    num/=10;
  }
  return s;
}


int main (int argc, char *argv[])
{
  int sum=  0, num;
  FILE *fin;

  fin = fopen (argv[1], "r"); /*opens the file with the name argv[1]  */
  
  
  while (fscanf(fin, "%d", &num) !=EOF){
    /* read integers from fin and store in the number */  
    sum = digit_sum(num);  
    printf ("The sum of digits of number is %d\n", sum);
 
  }
  
  
  
  fclose (fin);  /*closes the file */
  
  return 0;
}
