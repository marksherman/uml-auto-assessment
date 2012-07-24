/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 30: Call Reference              */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
void swap(int *a, int *b);
int main( int argc, char *argv[])
{

  int x, y;

  printf("please enter two integers\n");
  scanf("%d%d", &x, &y);
  
  swap( &x, &y);

  printf( "integers swapped: %d %d\n", x, y);
  
  
  return 0;

}

void swap( int *a, int *b){

  int temp;

  temp = *a;
  *a = *b;
  *b = temp;

  return ;
}

