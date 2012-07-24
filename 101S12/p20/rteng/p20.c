/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p20.c                          */
/*                         Due: 3/1/12                            */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/
#include <stdio.h>
int main(int argc, char* argv[])
{
  int i;
  /*declares i as integer type*/
  for (i=argc-1; i>=0; i--){
    /*starts for loop so that it runs argc times*/
    printf("%s\n", argv[i]);}
    /*prints out the string from argument line*/
    /*last argument will be printed first*/  
  return 0;
}
