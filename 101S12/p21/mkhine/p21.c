/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : scanf returns what?              */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include <stdio.h>
int main(int argc, char* argv[])
{
  int i;
  FILE *fin;
  fin=fopen("testdata21", "r");  /*this opens the file testdata 21 */
  while (fscanf (fin, "%d", &i)!= EOF)
    printf ("%d", i);
  putchar('\n');
  return 0;
}
