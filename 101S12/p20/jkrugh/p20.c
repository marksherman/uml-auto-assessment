/*******************************************************************/
/* Programmer: Jeremy Krugh                                        */
/*                                                                 */
/* Program 20: Reverse the Command Line                            */
/*                                                                 */
/* Approximate time of completion: 15 minutes                      */
/*******************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int i;

  for(i=argc-1; i<argc; i--)
  printf("%s\n", argv[i]);

  return 0;
}
