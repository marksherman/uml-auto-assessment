/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Solid Box of Astericks                                           */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int i;
  int j;
  int L;
  int H;
  printf("Please enter the the length and height of the box respectively:\n");
  /* prompts the user */
  scanf("%d %d", &L, &H);
  /* stores the values of the length and height in variables L and H */
  for (i=1; i<=H; i++){
  /* prints a line of astericks H times */
    for (j=1; j<=L; j++){
      printf("*");
      /* prints a line of L astericks */
    }
    printf("\n");
  }
  return 0 ;
}
