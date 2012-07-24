/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Reverse the Command Line                                         */
/*                                                                           */
/* Approximate completion time: 20 minutes                                   */
/*****************************************************************************/

#include <stdio.h>
int main( int argc, char *argv[] )
{
  int i;
  for(i=argc-1; i>=0; i--){
  /* runs the loop once for each command line argument */
    printf("%s\n", argv[i]);
    /* prints the 'i'th command line argument and a new line */
  }
  return 0 ;
}
