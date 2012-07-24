/*****************************************/
/* Programmer: Joe LaMarca               */
/* Program: p19 argv                     */
/* Approximate time of completion 5 min  */
/*****************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x;

  for(x=0;x<argc;x++)
    printf("%s\n",argv[x]);

  return 0;
}
