/******************************************/
/*Programmer: Aezaz Vegamwala             */
/*                                        */
/*Ptogram 30: Call Reference              */
/*                                        */
/*Approximate completion time: 30 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
void swap(int *x, int *y);
int main( int argc, char *argv[])
{

  int a, b;

  printf("Please enter two integers\n");
  scanf("%d%d", &a, &b);

  swap( &a, &b);

  printf( "Numbers Swapped: %d %d\n", a, b);


  return 0;

}

void swap( int *x, int *y){

  int temp;

  temp = *x;
  *x = *y;
  *y = temp;

  return ;
}
