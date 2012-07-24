/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Unfilled Box                                                     */
/*                                                                           */
/* Approximate completion time: 40 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int i, j, k, L, H;
  printf("Please enter the the length and height of the box respectively:\n");
  /* prompts the user */
  scanf("%d %d", &L, &H);
  /* stores the values of the length and height in variables L and H */
  for (i=1; i<=H; i++){
  /* prints a line for every iteration of the loop until height H is reached */
    if ((i==1) || (i==H)){
    /* prints a line of astericks for the first and last lines */
      for (j=1; j<=L; j++){
	printf("*");
	/* prints a line of L astericks */
      }
      printf("\n");
      /* prints a new line */
    }
    else{ 
    /* for all other lines in between the following occurs */
      printf("*");
      /* prints an asterisk at the edge of the box */
      for (k=2;k<L;k++){
      /* runs  loop for the middle of the line */
	printf(" ");
	/* fills the middle of the line with space */
      }
      printf("*");
      /* prints another asterisk at the other edge of the box */
      printf("\n");
      /* prints a new line */
    }
  }
  return 0 ;
}
