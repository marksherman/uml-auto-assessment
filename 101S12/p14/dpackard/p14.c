/*************************/
/*     Danny Packard     */
/*    p14 sine function  */
/*    15 minutes         */
/*************************/
#include<stdio.h>
#include<math.h>
#include<stdlib.h>
int main(int argc, char*argv[]){
  int x;
  x=atoi(argv[1]);
  printf("%f\n",sin(x));
  return 0;
}
