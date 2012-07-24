/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 16: Count Characters    */
/*                                */
/*Approx. Completion Time: 15mins */
/*                                */
/**********************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char*argv[]){

  int count,a;

  a=0;

  printf("Type characters to be counted then hit control+d twice\n");

  while((count=getchar())!=EOF)

  a=a+1;

  printf("\n %d\n",a);  

  return 0;

}
