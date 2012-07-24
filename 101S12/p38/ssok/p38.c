/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 38: Recursive digit sum         */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int sum(int digits);
int main(int argc, char* argv[])
{

  FILE *testdata28;
  int ans;
  int num;

  testdata28 = fopen( argv[1], "r" );

  while(fscanf( testdata28, "%d", &num) != EOF){
    ans = sum(num);
    printf("%d", ans );
  }
  printf( "\n" );
  return 0;
}

int sum(int digits)
{

  if(digits < 10)
    return digits;
  else
    return (digits % 10 + sum(digits / 10));
}
