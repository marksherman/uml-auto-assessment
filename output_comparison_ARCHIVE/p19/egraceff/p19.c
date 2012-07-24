/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Argv                                                             */
/*                                                                           */
/* Approximate completion time: 30 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int i;
  for(i = 0; i< argc; i++){
  /* runs the loop once for each command line argument */
    printf("%s\n", argv[i]);
    /* prints the 'i'th  command line argument and a new line */
  }
  return 0 ;
}
