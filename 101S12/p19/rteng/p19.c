/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p19.c                          */
/*                         Due: 3/1/12                            */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/
#include <stdio.h>
int main(int argc, char* argv[])
{
  int i;
  /*declares i as integer type*/
  for (i=0; i<argc; i++){
    /*creates for loop that runs argc times because it starts at 0*/
    printf("%s\n", argv[i]);}
    /*prints the string from the arguments until all strings are printed out*/
  return 0;
}
