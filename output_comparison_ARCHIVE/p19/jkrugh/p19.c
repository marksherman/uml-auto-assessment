/********************************************************************/
/* Programmer: Jeremy Krugh                                         */
/*                                                                  */
/* Program 19: Argv                                                 */
/*                                                                  */
/* Approximate time of completion: 15 minutes                       */
/********************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int i;

  for (i=0; i<argc; i++)
  printf("%s\n",argv[i]);

  return 0;
}
