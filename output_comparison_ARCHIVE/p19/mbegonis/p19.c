/***************************************************************************************************/
/*                                                                                                 */
/*  Mike Begonis                                                                                   */
/*  Program p19                                                                                    */
/*  This program prints out each command line argument on a seperate line using a for loop.        */
/*  Approx Completion time: 5 minutes                                                              */
/***************************************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int i;
  for(i=0;i<argc;i++){
    printf("%s\n",argv[i]);
  }

  return 0;
}


