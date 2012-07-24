/*******************************************/
/*Jake Conaty                              */
/*Project 30                               */
/*apx time: 30                             */
/*******************************************/


#include <stdio.h>
#include <stdlib.h>

void swap(int *a, int *b);

int main(int argc, char *argv[]){

  int x, y;

  x=atoi( argv[1]);
  y=atoi( argv[2]);

  return  swap( &x, &y);

}

void swap(int *a, int *b){
  int temp;

  temp=*a;
  *a=*b;
  *b=temp;
  return;
}
