/**********************************************************************************************************************/
/*                                                                                                                    */
/*  Mike Begonis                                                                                                      */
/*  Program p20                                                                                                       */
/*  This program prints out each command line argument on a new line in reverse order using argv and a for loop.      */
/*  Approx Completion Time: 6 minutes                                                                                 */
/**********************************************************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int i;

  for(i=argc-1;i>=0;i--){
    printf("%s\n",argv[i]);
  }

  return 0;
}
