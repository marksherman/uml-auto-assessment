/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p15:Asteriks                   */
/*                                        */
/* Approximate completion time:30 minutes */
/******************************************/
#include <stdio.h>

int main()
{

  int L, H, i, j;

 
  printf( "Please enter two numbers between 1 and 21:\n" );
  scanf( "%d%d", &L, &H );

  for( i = 0; i < L; i++ ){
    printf("\n");
   for( j = 0; j < H; j++ )
    printf( "*" );
  }
  
  printf("\n");

  return 0;

}
