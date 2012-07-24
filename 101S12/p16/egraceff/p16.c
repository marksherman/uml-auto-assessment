/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Count Characters                                                 */
/*                                                                           */
/* Approximate completion time: 1 hour 30 minutes                            */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int i;
  printf("Please enter the characters you wish to count followed by control d (otherwise known as EOF, which allows the program to detect when it should stop counting):\n");
  /* prompts the user */
  for(i=0; getchar()!=EOF; i++){ 
  }
  /* adds to the count of characters, i, until EOF is reached */
  printf("\nYou entered %d characters.\n", i);
  /* prints the number of characters */
  return 0 ;
}
