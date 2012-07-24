/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Sine Function                     */
/*                                              */
/*     Time to Completion: 15 Minutes           */
/*                                              */
/************************************************/

#include<stdio.h>
#include<math.h>
#include<stdlib.h>

int main(int argc, char* argv[]){

  printf("The sine of your number is: %f \n",sin( atof(argv[1]) ) );

  return(0);

}

