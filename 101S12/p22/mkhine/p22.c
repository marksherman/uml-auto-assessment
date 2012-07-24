/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Sum of a bunch                   */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include<stdio.h>
int main (int argc, char* argv[])
{
  int i = 0,sum = 0;
  FILE *fin;
 
  fin = fopen("testdata22", "r"); /*opens testdata 22 */
  while (fscanf (fin, "%d", &i)!= EOF)
    sum = sum + i;
  printf ("The sum of the numbers is %d\n", sum); /*prints out the sum */

  return 0;
}
