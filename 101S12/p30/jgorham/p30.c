/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 30                                                                   */
/*                                                                              */
/* Approximate Completion Time:  5 min                                          */
/********************************************************************************/

#include <stdio.h>
#include <stdlib.h>

int swap(int* a, int* b);

int main(int argc, char* argv[]){
  int x = 0;
  int y = 0;
  x = atoi(argv[1]);
  y = atoi(argv[2]);
  swap( &x , &y );
  printf("%d %d\n",x , y);
  return 0;
}

int swap(int* a, int* b){
  int tmp = 0;
  tmp = *b;
  *b = *a;
  *a = tmp;
  return 0;
}
