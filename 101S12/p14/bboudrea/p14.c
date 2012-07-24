/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment 14: Sine function              */
/*                                           */
/* Estimated time of Completion: 30 minutes  */
/*                                           */
/*********************************************/

#include<stdio.h>
#include<stdlib.h>
#include<math.h>

int main(int argc, char* argv[]){
  float deg=atof(argv[1]);
    printf("sin(%f)=%f\n",deg,sin(deg));

  return(0);
}
