/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Find the average                 */
/*                                                */
/*Approximate completion time: 10 minutes         */
/**************************************************/
#include<stdio.h>
int main(int argc, char*argv[])
{
  float sum;
  int a=0 ,number;
  FILE*fin;
  fin=fopen("testdata24","r");
  for (a = 0; a < 4; a++){
    fscanf(fin, "%d", &number);
    sum += number;
  }
  
  printf ("The average is %f\n", sum / 4);
  
  return 0;
}


